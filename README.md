# 杀戮尖塔 爱丽丝 Mod

本仓库是一个 [**杀戮尖塔**](https://store.steampowered.com/app/646570/Slay_the_Spire/) 模组，
添加了一名新的可选角色 [**爱丽丝·玛格特罗伊德**](https://zh.moegirl.org.cn/%E7%88%B1%E4%B8%BD%E4%B8%9D%C2%B7%E7%8E%9B%E6%A0%BC%E7%89%B9%E7%BD%97%E4%BE%9D%E5%BE%B7/)。

爱丽丝是居住在魔法之森的魔法使，拥有操控人偶程度的能力。

## 依赖模组

- [ModTheSpire](https://steamcommunity.com/sharedfiles/filedetails/?id=1605060445)
- [BaseMod](https://steamcommunity.com/sharedfiles/filedetails/?id=1605833019)
- [StSLib](https://steamcommunity.com/sharedfiles/filedetails/?id=1609158507)

### 可选依赖

- [魔理沙](https://steamcommunity.com/sharedfiles/filedetails/?id=1614104912)
- [随机数预测大师](https://steamcommunity.com/sharedfiles/filedetails/?id=3423569652)

## 核心机制：人偶

爱丽丝可以操控 **人偶**，**人偶** 们会帮助爱丽丝进行战斗，提供各种功能。

爱丽丝拥有七个 **人偶** 栏位，每个栏位的 **人偶** 可以为爱丽丝承受一名敌人的攻击伤害，溢出的伤害仍由爱丽丝承受。

**人偶** 造成的伤害受目标的 **易伤** 等效果影响，但不受爱丽丝的 **虚弱** 等效果影响。
类似地，**人偶** 的格挡也不受爱丽丝的 **脆弱** 或 **无法获得格挡** 等效果影响。

爱丽丝的部分卡牌可以指向 **人偶** 或栏位打出，并产生不同的效果。

- **生成**：在指定位置放置 **人偶**。如果该位置已有 **人偶**，则会先将其 **回收**。
- **回收**：移除 **人偶**，并可以触发一些特效。
- **行动**：使 **人偶** 发动特定效果。
- **指令**：指向 **人偶** 打出时，部分牌会触发一些额外效果。

## 致谢
  - Thanks to the [Marisa Mod](https://github.com/lf201014/STS_ThMod_MRS), which teaches me how to develop a mod of STS.
