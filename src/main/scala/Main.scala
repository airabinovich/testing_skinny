import models._
import support._

object Main extends App with Connection with DatasourceConfig  {

  val contact1 = Contact.save("contact1",
    Seq(
      RoleCommand("role1", Seq(12345, 12321)),
      RoleCommand("role2", Seq(54321, 12321)),
      RoleCommand("role3", Seq(12345, 54321)),
    )
  )

  // retrieving a contact won't fill the phones number inside each role
  println(s"My Contact: ${Contact.findById(contact1)}")

  // retrieving a role will fill the phones list
  println(s"All Roles: ${Role.findAll()}")

}
