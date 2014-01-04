# Scala Pet Store API
- Scala 2.10.x
- DDDに準拠した設計方針
- REST like API

##エンティティ(集約)
- [Account](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/j5ik2o/spetstore/domain/account/Account.scala)
- [Category](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/j5ik2o/spetstore/domain/item/Category.scala)
- [ItemType](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/j5ik2o/spetstore/domain/item/ItemType.scala)
- [Item](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/j5ik2o/spetstore/domain/item/Item.scala)
- [Cart](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/j5ik2o/spetstore/domain/purchase/Cart.scala)
- [Order](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/j5ik2o/spetstore/domain/purchase/Order.scala)

[CartSpec](https://github.com/j5ik2o/spetstore/blob/master/src/test/scala/com/j5ik2o/spetstore/domain/purchase/CartSpec.scala), [OrderSpec](https://github.com/j5ik2o/spetstore/blob/master/src/test/scala/com/j5ik2o/spetstore/domain/purchase/OrderSpec.scala)あたりがみどころ。

## リポジトリ
- [JDBC版](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/j5ik2o/spetstore/infrastructure/support/RepositoryOnJDBC.scala)
- [メモリ版](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/j5ik2o/spetstore/infrastructure/support/RepositoryOnMemory.scala)
- キャッシュマネジメント版(TODO)

## TODO
- アプリケーション層の実装
    - コントローラとアプリケーションサービスの実装
