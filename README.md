# ZON-BATTLE2

## plugin.yml

ビルド時に自動生成されます。[build.gradle.kts](build.gradle.kts) の以下の箇所で設定できます。
書き方は https://github.com/Minecrell/plugin-yml の Bukkit kotlin-dsl を見てください。

```kotlin
configure<BukkitPluginDescription> {
    // 内容
}
```

## タスク

### プラグインのビルド `build`

`build/libs` フォルダに `.jar` を生成します。

### テストサーバーの起動 `buildAndLaunchServer`

`:25565` でテストサーバーを起動します。
