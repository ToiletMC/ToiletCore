# ToiletCore - 厕所核心

# 模块介绍

## DebugStick

因为玩家可以生存模式合成调试棒，所以该模块可以限制一些逆天操作。

### 合成配方

![image](https://user-images.githubusercontent.com/77124888/175134422-a4c1c9c8-3ab6-4693-9fec-0f3cfa30e17e.png)

### 调试棒必备的权限：

- minecraft.debugstick
- minecraft.debugstick.always

## EffectOnBlock

该模块主要是实现温泉插件，也可以用来干点别的事情…

## LagAlter

这是一个滞后提醒器，当检测到服务器滞后时，会通过提醒的方式识图让玩家自觉维护服务器稳定性。插件会在启动时自动开启检测，无需手动配置。
通过挂钩 Spark，获取服务器最近一分钟的最大 mspt。

## Placeholder

为了更好的性能，抛弃 JavaScript。

- `%toilet_emoji_world%`：玩家名字前面的 emoji（仅限资源世界）
- `%toilet_emoji_tps%`： 服务器负荷的 emoji
- `%toilet_emoji_worldtime%`：世界时间的 emoji

## Premium

自动给予正版奖励。检测玩家的 UUID 类型是否为 v4，如果是离线玩家 UUID 类型为 v3。该方法非常安全且不需要发出任何网络请求。

## Shart

拉屎……

- `shart, shit, poop, poo`：上厕所
- `shart [玩家] [类型]`：强制别人上厕所（类型：rainbow）

## 代办列表

- [x] [DebugStickSupport](https://github.com/ToiletMC/plugin-DebugStickSupport)
- [x] [ToiletPAPI](https://github.com/ToiletMC/plugin-ToiletPAPI)
- [x] [AuthmeEncryptionConverter](https://github.com/ToiletMC/plugin-AuthmeEncryptionConverter)
- [x] [LagManager](https://github.com/ToiletMC/plugin-LagManager)
- [x] [Shart](https://github.com/ToiletMC/plugin-Shart)
- [x] [EffectOnBlock](https://github.com/ToiletMC/plugin-EffectOnBlock)
- [x] [AntiEndermanMushroom](https://github.com/ToiletMC/plugin-AntiEndermanMushroom)
- [x] [ZBYZ](https://github.com/ToiletMC/plugin-ZBYZ)
- [ ] [CDK](https://github.com/ToiletMC/plugin-CDK)
- [ ] [SignItem](https://github.com/ToiletMC/plugin-SignItem)
- [x] [EggRespawn](https://github.com/XXY233/EggRespawn)

## Known Issues

- [ ] Hook 无法读取配置文件
