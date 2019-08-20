package models

case class Friend(override val id: Long,
                  override val name: String,
                  override val roles: Seq[Role] = Nil,
                  override val nickname: String,
                 ) extends Contact with PersonWithNickname

object Friend {
  def from(c: Contact, pwn: PersonWithNickname): Friend = if (c.id == pwn.id) new Friend(
    id = c.id,
    name = c.name,
    roles = c.roles,
    nickname = pwn.nickname
  ) else throw new IllegalArgumentException("ids must match")
}
