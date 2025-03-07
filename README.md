# ToiletCore - 厕所核心

## 模块速览

| 模块              | 简介                                                                                               |
|-----------------|--------------------------------------------------------------------------------------------------|
| AntiEnderman    | 针对末影人在世界大面积传播蘑菇的问题进行了修复                                                                          |
| AntiChunkLoader | 禁止区块加载器                                                                                          |
| Authme          | 为 Authme 提供 DynamicBungeeAuth 的密码算法支持                                                            |
| CDK             | 自动发卡系统                                                                                           |
| DebugStick      | 因为服务器允许玩家可以生存模式合成调试棒，所以该模块可以限制一些逆天操作。                                                            |
| EffectOnBlock   | 实现主城温泉                                                                                           |
| EggRespawn      | 在每次末影龙死后都给予龙蛋                                                                                    |
| Hook            | ？？                                                                                               |
| LagAlert        | 这是一个滞后提醒器，当检测到服务器滞后时，会通过提醒的方式识图让玩家自觉维护服务器稳定性。插件会在启动时自动开启检测，无需手动配置。通过挂钩 Spark，获取服务器最近一分钟的最大 mspt。 |
| Premium         | 自动给予正版奖励。检测玩家的 UUID 类型是否为 v4，如果是离线玩家 UUID 类型为 v3。该方法非常安全且不需要发出任何网络请求。                            |
| Shart           | 上厕所                                                                                              |
| QQ              | 只是提供命令的tab，实际命令由QueQiao处理。                                                                       |
| TpGuard         | 巡逻守卫者，杜绝胡乱传送                                                                                     |
| BetterDrops     | 更好的掉落物，让隐形物品展示框可以掉落                                                                              |

## Luckperms 元数据

| key                           | 简介                            |
|-------------------------------|-------------------------------|
| toilet.setting.receive_potato | `true` 或 `false`，是否接收服务器尸体到背包 |

## 插件文档

### 指令

- `/toiletcore reload`：重载插件
- `/toiletcore debug`：调试信息

## 模块：DebugStick

### 必备权限

- minecraft.debugstick
- minecraft.debugstick.always

### 合成配方

![image](https://user-images.githubusercontent.com/77124888/175134422-a4c1c9c8-3ab6-4693-9fec-0f3cfa30e17e.png)

## 模块：Shart

### 指令

- `shart, shit, poop, poo`：上厕所
- `shart [玩家] [类型]`：强制别人上厕所（类型：rainbow）

## 已知问题

- 暂无
