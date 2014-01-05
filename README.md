# Scala Pet Store API (開発中)

## 目的
DDDに基づいた、一つの実装を示すこと(DDDは設計思想なので具体的な実装方式は複数あり得ますが、私が考える最良の実装という意味)。

## 特徴
- Scala 2.10.x
- DDDに準拠した設計方針
- REST like API

## レイヤー構造

- アプリケーション層
- ドメイン層
- インフラストラクチャ層

もっとも重要なのは、メンタルモデルを反映したドメイン層です。

## ドメイン層

### エンティティ(集約)と値オブジェクト
- [Account](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/github/j5ik2o/spetstore/domain/account/Account.scala)
    - AccountStatus
    - AccountProfile
    - AccountConfig
- [Category](https://github.com/github/j5ik2o/spetstore/blob/master/src/main/scala/com/github/j5ik2o/spetstore/domain/item/Category.scala)
- [ItemType](https://github.com/github/j5ik2o/spetstore/blob/master/src/main/scala/com/github/j5ik2o/spetstore/domain/item/ItemType.scala)
- [Item](https://github.com/github/j5ik2o/spetstore/blob/master/src/main/scala/com/github/j5ik2o/spetstore/domain/item/Item.scala)
- [Cart](https://github.com/github/j5ik2o/spetstore/blob/master/src/main/scala/com/github/j5ik2o/spetstore/domain/purchase/Cart.scala)
    - CartItem
- [Order](https://github.com/github/j5ik2o/spetstore/blob/master/src/main/scala/com/github/j5ik2o/spetstore/domain/purchase/Order.scala)
    - OrderItem

[CartSpec](https://github.com/github/j5ik2o/spetstore/blob/master/src/test/scala/com/github/j5ik2o/spetstore/domain/purchase/CartSpec.scala), [OrderSpec](https://github.com/github/j5ik2o/spetstore/blob/master/src/test/scala/com/github/j5ik2o/spetstore/domain/purchase/OrderSpec.scala)あたりがみどころ。


### DDD基盤コード
- [Entity](https://github.com/github/j5ik2o/spetstore/blob/master/src/main/scala/com/github/j5ik2o/spetstore/infrastructure/support/Entity.scala)
- [Repository](https://github.com/github/j5ik2o/spetstore/blob/master/src/main/scala/com/github/j5ik2o/spetstore/infrastructure/support/Repository.scala)
    - [RepositoryOnJDBC](https://github.com/github/j5ik2o/spetstore/blob/master/src/main/scala/com/github/j5ik2o/spetstore/infrastructure/support/RepositoryOnJDBC.scala)  
    JDBCに対応したリポジトリの骨格実装。[ScalikeJDBC](http://scalikejdbc.org/)で実装。
    - [RepositoryOnMemory](https://github.com/github/j5ik2o/spetstore/blob/master/src/main/scala/com/github/j5ik2o/spetstore/infrastructure/support/RepositoryOnMemory.scala)  
    メモリに対応したリポジトリの骨格実装。内部実装はMapですがRepositoryとして操作できる。
    - RepositoryOnMemcached(TODO)  
    Memcachedに対応したリポジトリの骨格実装。
    - CacheManagementRepository(TODO)  
    キャッシュのマネジメントを行うリポジトリ実装。

## アプリケーション層
- コントローラ(TODO)
- アプリケーションサービス(TODO)
