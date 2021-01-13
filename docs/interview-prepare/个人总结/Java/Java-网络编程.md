# Java - 网络编程完全总结

[尘语凡心](https://www.jianshu.com/u/a82533b0437e)关注

42016.10.08 13:12:27字数 6,337阅读 19,011

> 本文主要是自己在网络编程方面的学习总结，先主要介绍计算机网络方面的相关内容，包括计算机网络基础，OSI参考模型，TCP/IP协议簇，常见的网络协议等等，在此基础上，介绍Java中的网络编程。



```undefined
一、概述
二、计算机网络
   1.网络协议
   2.网络体系结构
三、OSI参考模型
四、TCP/IP参考模型
五、常见网络协议
   1.TCP协议
   2.UDP协议
   3.HTTP协议
六、计网常见问题
七、Java网络编程
```

### 一、概述

计算机网络是通过传输介质、通信设施和网络通信协议，把分散在不同地点的计算机设备互连起来，实现资源共享和数据传输的系统。网络编程就就是编写程序使联网的两个(或多个)设备(例如计算机)之间进行数据传输。Java语言对网络编程提供了良好的支持，通过其提供的接口我们可以很方便地进行网络编程。下面先对网络编程的一些基础知识进行介绍，最后给出使用Java语言进行网络编程的实例。

### 二、计算机网络

计算机网络20世纪60年代出现，经历了20世纪70年代、80年代和90年代的发展，进入21世纪后，计算机网络已经成为信息社会的基础设施，深入到人类社会的方方面面，与人们的工作、学习和生活息息相关。

> 网络协议

如同人与人之间相互交流是需要遵循一定的规矩一样，计算机之间能够进行相互通信是因为它们都共同遵守一定的规则，即网络协议。

> 网络体系结构

计算机网络是个复杂的系统，按照人们解决复杂问题的方法，把计算机网络实现的功能分到不同的层次上，层与层之间用接口连接。通信的双方具有相同的层次，层次实现的功能由协议数据单元(PDU)来描述。不同系统中的同一层构成对等层，对等层之间通过对等层协议进行通信，理解彼此定义好的规则和约定。

计算机网络体系结构是计算机网络层次和协议的集合，网络体系结构对计算机网络实现的功能，以及网络协议、层次、接口和服务进行了描述，但并不涉及具体的实现。接口是同一节点内相邻层之间交换信息的连接处，也叫服务访问点(SAP)。

![img](https://upload-images.jianshu.io/upload_images/2243690-5b18d16a7b653a66.png?imageMogr2/auto-orient/strip|imageView2/2/w/595/format/webp)

计算机网络层次模型

### 三、OSI参考模型

前面我们介绍了计算机网络的体系结构，因为计算机网络是个复杂的系统，所以把计算机网络实现的功能分到不同的层次上，而计算机网络体系结构是计算机网络层次和协议的集合。那么，计算机网络如何进行分层呢？下面先介绍的是OSI参考模型。

> 简介

世界上第一个网络体系结构由IBM公司提出（1974年，SNA），以后其他公司也相继提出自己的网络体系结构如：Digital公司的DNA，美国国防部的TCP/IP等，多种网络体系结构并存，其结果是若采用IBM的结构，只能选用IBM的产品，只能与同种结构的网络互联。

为了促进计算机网络的发展，国际标准化组织ISO于1977年成立了一个委员会，在现有网络的基础上，提出了不基于具体机型、操作系统或公司的网络体系结构，称为开放系统互连参考模型，即OSI/RM (Open System Interconnection Reference Model)。OSI模型把网络通信的工作分为7层，分别是物理层、数据链路层、网络层、传输层、会话层、表示层和应用层。

![img](https://upload-images.jianshu.io/upload_images/2243690-a1b19d383354abe1.png?imageMogr2/auto-orient/strip|imageView2/2/w/491/format/webp)

OSI参考模型的7个层次

> OSI模型层次功能

- **物理层**

物理层处于OSI的最底层，是整个开放系统的基础。物理层涉及通信信道上传输的原始比特流(bits)，它的功能主要是为数据端设备提供传送数据的通路以及传输数据。

- **数据链路层**

数据链路层的主要任务是实现计算机网络中相邻节点之间的可靠传输，把原始的、有差错的物理传输线路加上数据链路协议以后，构成逻辑上可靠的数据链路。需要完成的功能有链路管理、成帧、差错控制以及流量控制等。其中成帧是对物理层的原始比特流进行界定，数据链路层也能够对帧的丢失进行处理。

- **网络层**

网络层涉及源主机节点到目的主机节点之间可靠的网络传输，它需要完成的功能主要包括路由选择、网络寻址、流量控制、拥塞控制、网络互连等。

- **传输层**

传输层起着承上启下的作用，涉及源端节点到目的端节点之间可靠的信息传输。传输层需要解决跨越网络连接的建立和释放，对底层不可靠的网络，建立连接时需要三次握手，释放连接时需要四次挥手。

- **会话层和表示层**

会话层的主要功能是负责应用程序之间建立、维持和中断会话，同时也提供对设备和结点之间的会话控制，协调系统和服务之间的交流，并通过提供单工、半双工和全双工3种不同的通信方式，使系统和服务之间有序地进行通信。

表示层关心所传输数据信息的格式定义，其主要功能是把应用层提供的信息变换为能够共同理解的形式，提供字符代码、数据格式、控制信息格式、加密等的统一表示。

- **应用层**

应用层为OSI的最高层，是直接为应用进程提供服务的。其作用是在实现多个系统应用进程相互通信的同时，完成一系列业务处理所需的服务。

### 四、TCP/IP参考模型

OSI参考模型的初衷是提供全世界范围的计算机网络都要遵循的统一标准，但是由于存在模型和协议自身的缺陷，迟迟没有成熟的产品推出。TCP/IP协议在实践中不断完善和发展取得成功，作为网络的基础，Internet的语言，可以说没有TCP/IP协议就没有互联网的今天。

> 简介

TCP/IP，即Transmission Control Protocol/Internet Protocol的简写，中译名为传输控制协议/因特网互联协议，是Internet最基本的协议、Internet国际互联网络的基础。

TCP/IP协议是一个开放的网络协议簇，它的名字主要取自最重要的网络层IP协议和传输层TCP协议。TCP/IP协议定义了电子设备如何连入因特网，以及数据如何在它们之间传输的标准。TCP/IP参考模型采用4层的层级结构，每一层都呼叫它的下一层所提供的协议来完成自己的需求，这4个层次分别是：网络接口层、互联网层(IP层)、传输层(TCP层)、应用层。

![img](https://upload-images.jianshu.io/upload_images/2243690-713fa2f44e2f31dc.png?imageMogr2/auto-orient/strip|imageView2/2/w/528/format/webp)

OSI 和 TCP/IP模型对比

> TCP/IP模型层次功能

- **网络接口层**

TCP/IP协议对网络接口层没有给出具体的描述，网络接口层对应着物理层和数据链路层。

- **互联网层 ( IP层 ) **

互联网层是整个TCP/IP协议栈的核心。它的功能是把分组发往目标网络或主机。同时，为了尽快地发送分组，可能需要沿不同的路径同时进行分组传递。因此，分组到达的顺序和发送的顺序可能不同，这就需要上层必须对分组进行排序。互联网层除了需要完成路由的功能外，也可以完成将不同类型的网络（异构网）互连的任务。除此之外，互联网层还需要完成拥塞控制的功能。

- **传输层 ( TCP层 ) **

TCP层负责在应用进程之间建立端到端的连接和可靠通信，它只存在与端节点中。TCP层涉及两个协议，TCP和UDP。其中，TCP协议提供面向连接的服务，提供按字节流的有序、可靠传输，可以实现连接管理、差错控制、流量控制、拥塞控制等。UDP协议提供无连接的服务，用于不需要或无法实现面向连接的网络应用中。

- **应用层**

应用层为Internet中的各种网络应用提供服务。

### 五、常见网络协议

上面主要介绍了OSI参考模型和TCP/IP模型的相关内容，从下面这张图可以看出TCP/IP协议簇中不同的层次中有着很多不同的网络协议，下面主要介绍传输层的TCP、UDP协议和应用层的HTTP协议。

![img](https://upload-images.jianshu.io/upload_images/2243690-9c5180336d0431cf.png?imageMogr2/auto-orient/strip|imageView2/2/w/563/format/webp)

> TCP协议

- **简介**

TCP（Transmission Control Protocol ，传输控制协议）是面向连接的传输层协议。TCP层是位于IP层之上，应用层之下的中间层。不同主机的应用层之间经常需要可靠的、像管道一样的连接，但是IP层不提供这样的流机制，而是提供不可靠的包交换。TCP协议采用字节流传输数据。

- **TCP报文段格式**

TCP报文段包括协议首部和数据两部分，协议首部的固定部分有20个字节，首部的固定部分后面是选项部分。

![img](https://upload-images.jianshu.io/upload_images/2243690-bca295be64863fcf.png?imageMogr2/auto-orient/strip|imageView2/2/w/655/format/webp)

TCP报文段

下面是报文段首部各个字段的含义。

1. 源端口号以及目的端口号，各占2个字节，端口是传输层和应用层的服务接口，用于寻找发送端和接收端的进程，一般来讲，通过端口号和IP地址，可以唯一确定一个TCP连接，在网络编程中，通常被称为一个socket接口。
2. 序号，占4字节，用来标识从TCP发送端向TCP接收端发送的数据字节流。
3. 确认序号，占4字节，包含发送确认的一端所期望收到的下一个序号，因此，确认序号应该是上次已经成功收到数据字节序号加1.
4. 数据偏移，占4位，用于指出TCP首部长度，若不存在选项，则这个值为20字节，数据偏移的最大值为60字节。
5. 保留字段占6位，暂时可忽略，值全为0
6. 标志位
   URG（紧急） : 为1时表明紧急指针字段有效
   ACK（确认）：为1时表明确认号字段有效
   PSH（推送）：为1时接收方应尽快将这个报文段交给应用层
   RST（复位）：为1时表明TCP连接出现故障必须重建连接
   SYN（同步）：在连接建立时用来同步序号
   FIN （终止）： 为1时表明发送端数据发送完毕要求释放连接
7. 接收窗口占2个字节，用于流量控制和拥塞控制，表示当前接收缓冲区的大小。在计算机网络中，通常是用接收方的接收能力的大小来控制发送方的数据发送量。TCP连接的一端根据缓冲区大小确定自己的接收窗口值，告诉对方，使对方可以确定发送数据的字节数。
8. 校验和占2个字节，范围包括首部和数据两部分。
9. 选项是可选的，默认情况是不选。

- **三次握手与四次挥手**

TCP是面向连接的协议，因此每个TCP连接都有3个阶段：连接建立、数据传送和连接释放。连接建立经历三个步骤，通常称为“三次握手”。

TCP三次握手过程如下：

![img](https://upload-images.jianshu.io/upload_images/2243690-b51756778bea8db9.jpg?imageMogr2/auto-orient/strip|imageView2/2/w/600/format/webp)

TCP三次握手.jpg

1. 第一次握手
   客户机发送连接请求报文段到服务器，并进入SYN_SENT状态，等待服务器确认。（SYN = 1,seq=x）
2. 第二次握手
   服务器收到连接请求报文，如果同意建立连接，向客户机发回确认报文段，并为该TCP连接分配TCP缓存和变量。(SYN=1,ACK=1,seq=y,ack=x+1)。
3. 第三次握手
   客户机收到服务器的确认报文段后，向服务器给出确认报文段，并且也要给该连接分配缓存和变量。此包发送完毕，客户端和服务器进入ESTABLISHED（TCP连接成功）状态，完成三次握手。(ACK=1,seq=x+1,ack=y+1)。

TCP四次挥手过程如下：

![img](https://upload-images.jianshu.io/upload_images/2243690-58eb8315648d177e.png?imageMogr2/auto-orient/strip|imageView2/2/w/817/format/webp)

四次挥手.png

由于TCP连接是全双工的，因此每个方向都必须单独进行关闭。这原则是当一方完成它的数据发送任务后就能发送一个FIN来终止这个方向的连接。收到一个 FIN只意味着这一方向上没有数据流动，一个TCP连接在收到一个FIN后仍能发送数据。首先进行关闭的一方将执行主动关闭，而另一方执行被动关闭。

1. TCP客户端发送一个FIN，用来关闭客户到服务器的数据传送。
2. 服务器收到这个FIN，它发回一个ACK，确认序号为收到的序号加1。和SYN一样，一个FIN将占用一个序号。
3. 服务器关闭客户端的连接，发送一个FIN给客户端。
4. 客户端发回ACK报文确认，并将确认序号设置为收到序号加1。

> UDP协议

- **简介**

UDP，用户数据报协议，英文全称是User Datagram Protocol，它是TCP/IP协议簇中无连接的运输层协议。

- **UDP协议格式**

![img](https://upload-images.jianshu.io/upload_images/2243690-699105c373a98eb2.png?imageMogr2/auto-orient/strip|imageView2/2/w/750/format/webp)

UDP格式

从图中可以看到，UDP协议十分简单，它由两部分组成：首部和数据。其中，首部仅有8个字节，包括源端口和目的端口，长度（UDP用于数据报的长度）、校验和。

> HTTP协议

- **简介**

HTTP，超文本传输协议，英文全称是Hypertext Transfer Protocol，它是互联网上应用最为广泛的一种网络协议。HTTP是一种应用层协议，它是基于TCP协议之上的请求/响应式的协议，即一个客户端与服务器建立连接后，向服务器发送一个请求；服务器接到请求后，给予相应的响应信息。HTTP协议默认的端口号为80.

现在使用的HTTP协议是HTTP/1.1版本，1997年之前采用的是HTTP1.0版本。HTTP连接在1.0版本中采用非持续连接工作方式，1.1版本采用的是持续连接工作方式，持续连接是指服务器在发送响应后仍然在一段时间内保持这条由TCP运输层协议建立起来的连接，使客户机和服务器可以继续在这条连接上传输HTTP报文。

是否采用持续连接工作方式，1.0中默认是关闭的，需要在HTTP头加入"Connection:Keep-Alive"，才能启用Keep-Alive。HTTP1.1中默认启用Keep-Alive，如果加入"Connection:close"，才关闭。目前大部分浏览器都是用HTTP1.1协议，也就是说默认都会发起Keep-Alive的连接请求了，所以是否能完成一个完整的Keep- Alive连接就看服务器设置情况。

- **HTTP报文**

HTTP协议是基于TCP协议之上的请求/响应式协议，下面主要介绍HTTP报文的格式，HTTP报文主要有请求报文和响应报文两种。首先看请求报文的格式：

![img](https://upload-images.jianshu.io/upload_images/2243690-2791fc06cc0455a8.png?imageMogr2/auto-orient/strip|imageView2/2/w/389/format/webp)

HTTP请求报文格式

HTTP请求报文由请求行、首部行和实体主体组成，由浏览器发送给服务器。上面这张图中SP表示空格，cr lf表示回车和换行。

![img](https://upload-images.jianshu.io/upload_images/2243690-75cbcd5d58e6ccbc.png?imageMogr2/auto-orient/strip|imageView2/2/w/373/format/webp)

HTTP响应报文格式

上面这张图是HTTP响应报文，它由状态行、首部行和实体主体组成。下面两张图是在谷歌浏览器内访问服务器查看的HTTP请求和响应。

![img](https://upload-images.jianshu.io/upload_images/2243690-d4d7995e9be5c7a8.png?imageMogr2/auto-orient/strip|imageView2/2/w/556/format/webp)

HTTP请求报文例子

![img](https://upload-images.jianshu.io/upload_images/2243690-4bd51e413bdf79de.png?imageMogr2/auto-orient/strip|imageView2/2/w/554/format/webp)

HTTP响应报文例子

- **HTTP请求方法和响应状态码**

在上面的HTTP请求报文例子中，我们可以看到请求方法是GET，这表示请求读取由URL所标志的信息，除了GET，还有其它几种常用的方法。

![img](https://upload-images.jianshu.io/upload_images/2243690-dbbadcc5013da14a.png?imageMogr2/auto-orient/strip|imageView2/2/w/664/format/webp)

HTTP请求报文的一些方法

在HTTP响应报文的例子中，我们可以看到状态码是200，表示响应成功。下表是其它状态码，总共5大类，33种。

![img](https://upload-images.jianshu.io/upload_images/2243690-d9928a134314eeb0.png?imageMogr2/auto-orient/strip|imageView2/2/w/686/format/webp)

HTTP响应报文的状态码

> HTTPS和HTTP的区别

**[HTTP与HTTPS的区别](https://links.jianshu.com/go?to=https%3A%2F%2Fwww.cnblogs.com%2Fwqhwe%2Fp%2F5407468.html)**
**[什么是SSL，HTTPS是如何工作的？](https://links.jianshu.com/go?to=https%3A%2F%2Fwww.cnblogs.com%2Fehcoo%2Fp%2F6368304.html)**

HTTPS（全称：Hyper Text Transfer Protocol over Secure Socket Layer），是以安全为目标的HTTP通道，简单讲是HTTP的安全版。即HTTP下加入SSL层，HTTPS的安全基础是SSL，因此加密的详细内容就需要SSL。它是一个URI scheme（抽象标识符体系），句法类同http:体系。用于安全的HTTP数据传输。https:URL表明它使用了HTTP，但HTTPS存在不同于HTTP的默认端口及一个加密/身份验证层（在HTTP与TCP之间）。

超文本传输协议HTTP协议被用于在Web浏览器和网站服务器之间传递信息。HTTP协议以明文方式发送内容，不提供任何方式的数据加密，如果攻击者截取了Web浏览器和网站服务器之间的传输报文，就可以直接读懂其中的信息，因此HTTP协议不适合传输一些敏感信息，比如信用卡号、密码等。

为了解决HTTP协议的这一缺陷，需要使用另一种协议：安全套接字层超文本传输协议HTTPS。为了数据传输的安全，HTTPS在HTTP的基础上加入了SSL协议，SSL依靠证书来验证服务器的身份，并为浏览器和服务器之间的通信加密。

HTTPS和HTTP的区别主要为以下四点：
1）https协议需要到ca申请证书，一般免费证书很少，需要交费。
2）http是超文本传输协议，信息是明文传输，https 则是具有安全性的ssl加密传输协议。
3）http和https使用的是完全不同的连接方式，用的端口也不一样，前者是80，后者是443。
4）http的连接很简单，是无状态的；HTTPS协议是由SSL+HTTP协议构建的可进行加密传输、身份认证的网络协议，比http协议安全。

### 六、常见问题

到这里，关于计算机网络部分的总结内容就结束了，下面是几个常见的问题，汇总在这里。

1. OSI参考模型的分为哪几层，每层的功能？
   OSI，开放系统互连参考模型，它的7个层次自顶到下依次为应用层，表示层，会话层，传输层，网络层，数据链路层和物理层。各层的功能见文章开始。
2. TCP协议和UDP协议的区别？
   TCP协议是传输控制协议，UDP协议是用户数据报协议，两者都是传输层的协议，主要区别在于前者是可靠的，面向连接的协议，后者是不可靠的，无连接的协议。其它的区别还有，TCP协议传输速度慢，UDP常用于一次性传输比较少量数据的网络应用。
3. TCP三次握手为什么不能是两次?
   主要是防止两次握手情况下已经失效的连接请求报文段突然又传送到服务端而产生错误。例如，客户机A向服务器B发送TCP连接请求，第一个连接请求报文在网络的某个节点长时间滞留，A超时后认为报文丢失，于是再重传一次连接请求，B收到后建立连接。数据传输完毕后双方断开连接，而这时之前滞留的连接请求到达了服务端B，而B认为A又发来连接请求。如果两次握手建立连接，A并无连接请求，造成B的资源浪费。
4. HTTP请求的GET方法和POST方法的区别？
   GET和POST是HTTP请求的两种方法，主要区别在于GET方法是请求读取由URL所标志的信息，POST是给服务器添加信息。[点击查看更多](https://links.jianshu.com/go?to=http%3A%2F%2Fkb.cnblogs.com%2Fpage%2F188928%2F)
5. 在浏览器中输入网址到显示出页面的整个过程？
   (1) 输出包含域名的网址 (2) 浏览器向DNS请求解析域名对应的IP地址 (3) 域名系统DNS解析出域名对应的IP地址 (4) 浏览器与该服务器建立TCP连接 (5) 浏览器发送HTTP请求 (6) 服务器通过HTTP响应把页面文件发送给浏览器 (7) TCP连接释放 (8) 浏览器解释文件，并显示

### 七、Java网络编程

Java的网络编程主要涉及到的内容是Socket编程，那么什么是Socket呢？简单地说，Socket，套接字，就是两台主机之间逻辑连接的端点。TPC/IP协议是传输层协议，主要解决数据如何在网络中传输，而HTTP是应用层协议，主要解决如何包装数据。Socket，本质上就是一组接口，是对TCP/IP协议的封装和应用(程序员层面上)。

> 整体流程

Socket编程主要涉及到客户端和服务器端两个方面，首先是在服务器端创建一个服务器套接字(ServerSocket)，并把它附加到一个端口上，服务器从这个端口监听连接。端口号的范围是0到65536，但是0到1024是为特权服务保留的端口号，我们可以选择任意一个当前没有被其他进程使用的端口。

客户端请求与服务器进行连接的时候，根据服务器的域名或者IP地址，加上端口号，打开一个套接字。当服务器接受连接后，服务器和客户端之间的通信就像输入输出流一样进行操作。

![img](https://upload-images.jianshu.io/upload_images/2243690-e2ffda391807643e.png?imageMogr2/auto-orient/strip|imageView2/2/w/673/format/webp)

> 实例一

下面是一个客户端和服务器端进行数据交互的简单例子，客户端输入正方形的边长，服务器端接收到后计算面积并返回给客户端，通过这个例子可以初步对Socket编程有个把握。

- **服务器端**



```csharp
public class SocketServer {

    public static void main(String[] args) throws IOException {

        // 端口号
        int port = 7000;
        // 在端口上创建一个服务器套接字
        ServerSocket serverSocket = new ServerSocket(port);
        // 监听来自客户端的连接
        Socket socket = serverSocket.accept();

        DataInputStream dis = new DataInputStream(
                new BufferedInputStream(socket.getInputStream()));

        DataOutputStream dos = new DataOutputStream(
                new BufferedOutputStream(socket.getOutputStream()));

        do {

            double length = dis.readDouble();
            System.out.println("服务器端收到的边长数据为：" + length);
            double result = length * length;
            dos.writeDouble(result);
            dos.flush();

        } while (dis.readInt() != 0);

        socket.close();
        serverSocket.close();
    }
}
```

- **客户端**



```csharp
public class SocketClient {

    public static void main(String[] args) throws UnknownHostException, IOException {

        int port = 7000;

        String host = "localhost";

        // 创建一个套接字并将其连接到指定端口号
        Socket socket = new Socket(host, port);

        DataInputStream dis = new DataInputStream(
                new BufferedInputStream(socket.getInputStream()));

        DataOutputStream dos = new DataOutputStream(
                new BufferedOutputStream(socket.getOutputStream()));

        Scanner sc = new Scanner(System.in);

        boolean flag = false;

        while (!flag) {

            System.out.println("请输入正方形的边长:");
            double length = sc.nextDouble();

            dos.writeDouble(length);
            dos.flush();

            double area = dis.readDouble();

            System.out.println("服务器返回的计算面积为:" + area);

            while (true) {

                System.out.println("继续计算？(Y/N)");

                String str = sc.next();

                if (str.equalsIgnoreCase("N")) {
                    dos.writeInt(0);
                    dos.flush();
                    flag = true;
                    break;
                } else if (str.equalsIgnoreCase("Y")) {
                    dos.writeInt(1);
                    dos.flush();
                    break;
                }
            }
        }

        socket.close();
    }
}
```

> 实例二

可以看到上面的服务器端程序和客户端程序是一对一的关系，为了能让一个服务器端程序能同时为多个客户提供服务，可以使用多线程机制，每个客户端的请求都由一个独立的线程进行处理。下面是改写后的服务器端程序。



```java
public class SocketServerM {

    public static void main(String[] args) throws IOException {

        int port = 7000;
        int clientNo = 1;

        ServerSocket serverSocket = new ServerSocket(port);

        // 创建线程池
        ExecutorService exec = Executors.newCachedThreadPool();

        try {

            while (true) {
                Socket socket = serverSocket.accept();
                exec.execute(new SingleServer(socket, clientNo));
                clientNo++;
            }

        } finally {
            serverSocket.close();
        }

    }
}

class SingleServer implements Runnable {

    private Socket socket;
    private int clientNo;

    public SingleServer(Socket socket, int clientNo) {
        this.socket = socket;
        this.clientNo = clientNo;
    }

    @Override
    public void run() {

        try {

            DataInputStream dis = new DataInputStream(
                    new BufferedInputStream(socket.getInputStream()));

            DataOutputStream dos = new DataOutputStream(
                    new BufferedOutputStream(socket.getOutputStream()));

            do {

                double length = dis.readDouble();
                System.out.println("从客户端" + clientNo + "接收到的边长数据为：" + length);
                double result = length * length;
                dos.writeDouble(result);
                dos.flush();

            } while (dis.readInt() != 0);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("与客户端" + clientNo + "通信结束");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
```

上面改进后的服务器端代码可以支持不断地并发响应网络中的客户请求。关键的地方在于多线程机制的运用，同时利用线程池可以改善服务器程序的性能。

到这里，Java网络编程部分的内容就结束了，文中如有错误，欢迎指出。