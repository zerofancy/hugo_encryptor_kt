# Hugo Encryptor Kotlin

[Hugo Encryptor](https://github.com/Li4n0/hugo_encryptor) 的Kotlin版本。

原项目使用Python实现，而很多人比如我不喜欢Python，因为总觉得他的依赖环境很乱。于是我基于这个观点实现了Kotlin版本。

## 演示

[Demo](https://ntutn.top/hugo_encryptor_kt/posts/this-is-hugo-encryptor/)

## 安装使用方式（Linux/MacOS）

前置依赖：curl、java

将`hugow`下载到你的博客目录，然后直接用`./hugow`替换`hugo`命令。

```shell
curl -L -o hugow https://github.com/zerofancy/hugo_encryptor_kt/releases/latest/download/hugow
chmod +x hugow

./hugow
```

接下来写文章时，用下面的方式插入一个加密块：

```markdown
{{% hugo-encryptor "PASSWORD" %}}

这里是加密段落。

{{% /hugo-encryptor %}}
```

然后生成静态网页时记得使用`./hugow`命令而不是`hugo`命令，就大功告成了。

## 安装使用方式（Windows）
> 以下脚本在Windows 11 22H2测试通过。

```shell
curl -L -o hugow.bat https://github.com/zerofancy/hugo_encryptor_kt/releases/latest/download/hugow.bat
.\hugow.bat
```

接下来写文章时，用下面的方式插入一个加密块：

```markdown
{{% hugo-encryptor "PASSWORD" %}}

这里是加密段落。

{{% /hugo-encryptor %}}
```

然后生成静态网页时记得使用`.\hugow.bat`命令而不是`hugo`命令，就大功告成了。


## 致谢

本项目想法来源于 [Hugo Encryptor](https://github.com/Li4n0/hugo_encryptor)

项目中引用了以下第三方库：

- [Apache Commons Codec](https://commons.apache.org/proper/commons-codec/)
- [Bouncy Castle](https://www.bouncycastle.org/)
- [jsoup](https://jsoup.org/)

在此向以上项目作者和贡献者表示衷心感谢。
