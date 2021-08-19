package com.cmartin.learn

object OpaquePill {
  import CompanyIdOT._
  import ObjectIdOT._
  import DeviceNameOT._

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

  object DeviceNameOT {
    opaque type DeviceName = String

    object DeviceName {
      def apply(name: String): DeviceName = name
    }

    extension (name: DeviceName) {
      def toString: String = name
    }
  }

  case class Device(companyId: CompanyId, objectId: ObjectId, name: DeviceName)
}
