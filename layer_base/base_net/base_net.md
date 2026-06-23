# 网络组件


## 基本概念分类


### 缓存策略（五种，查看 CacheEnum 类）

- CACHE_ONLY
- NET_ONLY
- CACHE_AND_NET
- CACHE_ELSE_NET
- NET_ELSE_CACHE


### 网络状态（查看 NetworkState 类）

- mIsConnected，是否已连接
- mIsValidated，是否经过验证
- mIsMetered，是否为计费网络
- mIsNotRoaming，是否处于非漫游状态
- mNetworkType， 网络类型


### 网络类型（查看 NetworkType 类）

- NETWORK_WIFI
- NETWORK_5G
- NETWORK_4G
- NETWORK_3G
- NETWORK_2G
- NETWORK_UNKNOWN
- NETWORK_NO


## Api





