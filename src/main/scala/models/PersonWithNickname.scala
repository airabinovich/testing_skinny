package models

import scalikejdbc.WrappedResultSet
import skinny.orm.{Alias, SkinnyCRUDMapper}

trait PersonWithNickname {
  def id: Long

  def nickname: String
}

object PersonWithNickname extends SkinnyCRUDMapper[PersonWithNickname] {
  override val defaultAlias: Alias[PersonWithNickname] = createAlias("person_with_nickname")
  override val useAutoIncrementPrimaryKey: Boolean = false

  override def extract(rs: WrappedResultSet, n: scalikejdbc.ResultName[PersonWithNickname]): PersonWithNickname = new PersonWithNickname {
    override val id: Long = rs.long(n.id)

    override val nickname: String = rs.string(n.nickname)
  }

  def save(id: Long, nickname: String): Long = createWithAttributes(
    'id -> id,
    'nickname -> nickname
  )
}
