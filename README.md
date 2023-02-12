# Hugo Encryptor Kotlin

[Hugo Encryptor](https://github.com/Li4n0/hugo_encryptor) 的Kotlin版本。

原项目使用Python实现，而很多人比如我不喜欢Python，因为总觉得他的依赖环境很乱。于是我基于这个观点实现了Kotlin版本。

## 安装使用方式

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
