# ToiletCore - 厕所核心

## DebugStickSupport
允许玩家在生存模式合成调试棒  
### 权限授予命令
`/lp group default permisssion minecraft:debugstick true`  
`/lp group default permisssion minecraft:debugstick.always true`  
![image](https://user-images.githubusercontent.com/77124888/175134422-a4c1c9c8-3ab6-4693-9fec-0f3cfa30e17e.png)


## ToiletPAPI
为了更好的性能，抛弃JavaScript。  
### 实现的占位符
- [X] 世界名称：
  - [X] 导弹战争：🗡
  - [X] 资源世界：⛏
- [X] 服务器负荷emoji：
  - [X] TPS>=18：&a🌑
  - [X] TPS>=15：&6🌑
  - [X] TPS>=10：&c🌑
  - [X] TPS<10：&4🌑
- [X] 世界时间emoji：
  - [X] 0~12000 ticks：&6☀
  - [X] 其它：&e🌚
### PlaceholderAPI
`%toilet_emoji_world%`：玩家名字前面的 emoji  
`%toilet_emoji_tps%`：  服务器负荷的 emoji  
`%toilet_emoji_worldtime%`：世界时间的 emoji  
~~`%toilet_tab_footer%`：TAB 列表的页脚文字~~【该功能已被TAB插件替代。】


## LagManager
这是一个滞后提醒器，当检测到服务器滞后时，会通过提醒的方式识图让玩家自觉维护服务器稳定性。插件会在启动时自动开启检测，无需手动配置。
通过挂钩 Spark，获取服务器最近一分钟的最大 mspt。  
### 命令帮助
执行一次检测：`/lagmanager test`  
发送强制提醒：`/lagmanager force`  
显示debug信息：`/lagmanager debug`  
重载插件：`/lm reload`

## ZBYZ
自动给予正版奖励。检测玩家的UUID类型是否为v4，如果是离线玩家UUID类型为v3。该方法非常安全且不需要发出任何网络请求。  
命令：`/zbyz reload`  
从 config.yml 中重新加载检测到正版时需要执行的命令。


## 代办列表

- [X] [DebugStickSupport](https://github.com/ToiletMC/plugin-DebugStickSupport)
- [X] [ToiletPAPI](https://github.com/ToiletMC/plugin-ToiletPAPI)
- [X] [AuthmeEncryptionConverter](https://github.com/ToiletMC/plugin-AuthmeEncryptionConverter)
- [X] [LagManager](https://github.com/ToiletMC/plugin-LagManager)
- [X] [Shart](https://github.com/ToiletMC/plugin-Shart)
- [ ] [EffectOnBlock](https://github.com/ToiletMC/plugin-EffectOnBlock)
- [ ] [AntiEndermanMushroom](https://github.com/ToiletMC/plugin-AntiEndermanMushroom)
- [X] [ZBYZ](https://github.com/ToiletMC/plugin-ZBYZ)
- [ ] [CDK](https://github.com/ToiletMC/plugin-CDK)
- [ ] [SignItem](https://github.com/ToiletMC/plugin-SignItem)
