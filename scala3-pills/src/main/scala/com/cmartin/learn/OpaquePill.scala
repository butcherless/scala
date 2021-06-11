package com.cmartin.learn

object OpaquePill {

  import CompanyIdOT._
  import ObjectIdOT._

  object CompanyIdOT {

    opaque type CompanyId = Long

    object CompanyId {
      def apply(id: Long): CompanyId = id
    }

    extension (id: CompanyId) {
      def toLong: Long = id
    }
  }

  object ObjectIdOT {
    opaque type ObjectId = Long

    object ObjectId {
      def apply(id: Long): ObjectId = id
    }

    extension (id: ObjectId) {
      def toLong: Long = id
    }
  }


  case class Device(companyId: CompanyId, objectId: ObjectId)
}
