import models._
import support._

object Main extends App with Connection with DatasourceConfig  {

  val contact1Id = Contact.save("William",
    Seq(
      RoleCommand("role1", Seq(12345, 12321)),
      RoleCommand("role2", Seq(54321, 12321)),
      RoleCommand("role3", Seq(12345, 54321)),
    )
  )

  val person1Id = PersonWithNickname.save(contact1Id, "Bill")

  // retrieving a contact
  println(s"My Contact: ${Contact.findById(contact1Id)}")

  // retrieving a person with nickname
  println(s"Person With Nickname: ${Contact.findById(person1Id)}")

  // mixing the person with nickname and the contact to create a friend
  println(s"My Friend: ${Friend.from(Contact.findById(contact1Id).get, PersonWithNickname.findById(person1Id).get)}")

}
