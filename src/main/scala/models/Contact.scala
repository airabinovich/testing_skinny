package models

import scalikejdbc.WrappedResultSet
import scalikejdbc.interpolation.{SQLSyntax => sqls}
import skinny.orm.{Alias, SkinnyCRUDMapper, SkinnyJoinTable}

trait Contact {
  def id: Long

  def name: String

  def roles: Seq[Role] = Nil
}

object Contact extends SkinnyCRUDMapper[Contact] {
  override val defaultAlias: Alias[Contact] = createAlias("contact")

  val rolesRef = hasMany[Role](
    Role -> Role.defaultAlias,
    (contact, role) => sqls.eq(contact.id, role.contactId),
    (contact, roles) => new Contact {
      override val id: Long = contact.id
      override val name: String = contact.name
      override val roles: Seq[Role] = roles
    }
  ).includes[Role](
    merge = (contacts, roles) => contacts.map(c => new Contact {
      override val id: Long = c.id
      override val name: String = c.name
      override val roles: Seq[Role] = roles.filter(_.contactId == c.id)
    })
  ).byDefault

  /**
    * This will save a contact, all the roles in the list and all phones in each role's list
    *
    * @param name  the contact's name
    * @param roles a list of RoleCommand
    * @return the new contact id
    */
  def save(name: String, roles: Seq[RoleCommand]): Long = {
    val contactId = createWithNamedValues(column.name -> name)
    roles.map(roles => Role.save(contactId, roles.name, roles.phoneNumbers))
    contactId
  }

  override def extract(rs: WrappedResultSet, n: scalikejdbc.ResultName[Contact]): Contact = new Contact {
    override val id = rs.long(n.id)
    override val name = rs.string(n.name)
  }
}

case class RoleCommand(name: String, phoneNumbers: Seq[Int])

case class Role(id: Long, name: String, contactId: Long, phones: Seq[Phone] = Nil)

object Role extends SkinnyCRUDMapper[Role] {
  override val defaultAlias: Alias[Role] = createAlias("role")

  hasManyThrough[RolePhone, Phone](
    through = RolePhone -> RolePhone.defaultAlias,
    throughOn = (r, rp) => sqls.eq(r.id, rp.roleId),
    many = Phone -> Phone.defaultAlias,
    on = (rp, p) => sqls.eq(rp.phoneId, p.id),
    merge = (role, phones) => role.copy(phones = phones)
  ).byDefault

  def save(contactId: Long, name: String, phones: Seq[Int]): Long = {
    val roleId = createWithNamedValues(column.contactId -> contactId, column.name -> name)
    phones.map(number => Phone.save(number)).map(phoneId => RolePhone.save(roleId, phoneId))
    roleId
  }

  override def extract(rs: WrappedResultSet, n: scalikejdbc.ResultName[Role]): Role = new Role(
    id = rs.long(n.id),
    contactId = rs.long(n.contactId),
    name = rs.string(n.name),
  )
}

case class RolePhone(roleId: Long, phoneId: Long)

object RolePhone extends SkinnyJoinTable[RolePhone] {
  override def defaultAlias: Alias[RolePhone] = createAlias("role_phone")

  def save(roleId: Long, phoneId: Long): Any = createWithNamedValues(
    column.roleId -> roleId,
    column.phoneId -> phoneId,
  )
}

case class Phone(id: Long, number: Int)

object Phone extends SkinnyCRUDMapper[Phone] {
  override val defaultAlias: Alias[Phone] = createAlias("phone")

  def save(number: Int): Long = {
    findBy(sqls.eq(column.number, number)) match {
      case Some(phone) => phone.id
      case None => createWithNamedValues(column.number -> number)
    }
  }

  override def extract(rs: WrappedResultSet, n: scalikejdbc.ResultName[Phone]): Phone = new Phone(
    id = rs.long(n.id),
    number = rs.int(n.number),
  )
}
