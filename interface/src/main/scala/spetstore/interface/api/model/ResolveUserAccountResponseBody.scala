package spetstore.interface.api.model

case class ResolveUserAccountResponseBody(id: String,
                                          emailAddress: String,
                                          firstName: String,
                                          lastName: String,
                                          createdAt: Long,
                                          updatedAt: Option[Long])
