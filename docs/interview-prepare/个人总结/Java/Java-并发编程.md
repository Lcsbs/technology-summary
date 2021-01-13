# Java - 并发编程必知必会

[![img](https://upload.jianshu.io/users/upload_avatars/2243690/01cb1bae1c10.jpg?imageMogr2/auto-orient/strip|imageView2/1/w/96/h/96/format/webp)](https://www.jianshu.com/u/a82533b0437e)

[尘语凡心](https://www.jianshu.com/u/a82533b0437e)关注

0.8282016.09.17 14:00:17字数 3,937阅读 10,654



```undefined
一、概述
二、相关概念
三、Java多线程
  1.开启线程
  2.终止线程
四、线程安全
  1.线程问题
  2.常见锁机制
五、线程池
  1.概述及示例
  2.实现原理及源码分析
六、总结
```

### 一、概述

在操作系统的学习中我们知道，随着不断提出的新的应用需求，计算机体系结构的不断发展，操作系统也在不断地发展，从最初的单道批处理系统到多道批处理系统、分时系统和实时系统等等，不同的操作系统有着各自不同的特征，但是它们也都有着几个基本特征，其中之一就是并发。进程和并发是现代操作系统中最重要的基本概念，由于多核多线程CPU的诞生，为了充分利用CPU的资源，多线程、高并发的编程越来越受重视和关注。

### 二、相关概念

- **程序与进程**
  程序是一组有序指令的集合，是一种静态的概念。进程是程序的一次执行，属于一种动态的概念。在多道程序环境下，程序的执行属于并发执行，此时它们将失去封闭性，并具有间断性，运行结果也将不可再现，为了能使多个程序可以并发执行，提高资源利用率和系统吞吐量，并且可以对并发执行的程序加以描述和控制，引入进程的概念。
- **进程和线程**
  线程的引入主要是为了减少程序在并发执行时所付出的时空开销。我们知道，为了能使程序能够并发执行，系统必须进行创建进程、撤销进程以及进程切换等操作，而进程作为一个资源的拥有者，在进行这些操作时必须为之付出较大的时空开销。
  线程和进程的区别主要如下：(1) 进程是系统中拥有资源的一个基本单位，线程本身并不拥有系统资源，同一进程内的线程共享进程拥有的资源。(2) 进程仅是资源分配的基本单位，线程是调度和分派的基本单位。(3) 进程之间相对比较独立，彼此不会互相影响，而线程共享同一个进程下面的资源，可以互相通信影响。(4) 线程的并发性更高，可以启动多个线程执行同程序的不同部分。
- **并行和并发**
  并行是指两个或多个线程在**同一时刻** 执行，并发是指两个或多个线程在 **同一时间间隔**内发生。如果程序同时开启的线程数小于CPU的核数，那么不同进程的线程就可以分配给不同的CPU来运行，这就是并行，如果线程数多于CPU的核数，那就需要并发技术。

### 三、Java多线程

上面主要介绍了一些重要的相关概念，下面开始Java里面的多线程编程探究学习。Java虚拟机允许应用程序并发地运行多个执行线程，常见的开启新的线程的方法主要有3种。

> （推荐）任务类实现Runnable接口，在方法Run()里定义任务。



```java
/**
 * @author JackalTsc
 */
public class Main {

    public static void main(String[] args) {

       //将ThreadNew实例作为参数实例化Thread之后start启动线程
        new Thread(new ThreadNew()).start();

        System.out.println(" Thread Main ");
    }
}

// 实现Runnable接口并在方法run里定义任务
class ThreadNew implements Runnable {

    @Override
    public void run() {

        try { // 延时0.5秒
            Thread.sleep(500L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(" Thread New ");
    }
}
```

> 任务类继承Thread，重写run()方法。



```java
/**
 * @author JackalTsc
 */
public class Main {

    public static void main(String[] args) {

        new ThreadNew2().start();

        System.out.println(" Thread Main ");
    }
}

// 继承自类Thread并重写run方法
class ThreadNew2 extends Thread {

    @Override
    public void run() {
        try { // 延时0.5秒
            Thread.sleep(500L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(" Thread New2 ");
    }
}
```

> 实现接口Callable并在call()方法里得到线程执行结果。



```java
/**
 * @author JackalTsc
 */
public class Main {

    public static void main(String[] args) {

        FutureTask<String> futureTask = new FutureTask<>(new ThreadNew3());

        new Thread(futureTask).start();

        System.out.println(" Thread Main ");

        try {
            System.out.println("执行结果是 " + futureTask.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}

// 实现接口Callable并在call()方法里定义任务
class ThreadNew3 implements Callable<String> {

    @Override
    public String call() throws Exception {

        try { // 延时0.5秒
            Thread.sleep(500L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(" Thread New3 ");

        return "Thread New3 Result";
    }
}
```

上面三种就是Java中开启新的线程的方式，其中第1种，实现Runnable接口最常用，也最灵活，第2种，因为任务类必须继承自Thread，而Java中又仅支持单继承，所以有时不太方便，第3种方法主要是可以得到线程执行的返回结果。

开启的新线程都有一个线程优先级，代表该线程的重要程度，可以通过Thread类的getPriority()和setPriority()来得到或者设置线程的优先级。线程的优先级范围是1~10，默认情况下是5。

在线程创建完成还未启动的时候，我们可以通过方法setDaemon()来将线程设置为守护线程。守护线程，简单理解为后台运行线程，比如当程序运行时播放背景音乐。守护线程与普通线程在写法上基本没有区别，需要注意的是，当进程中所有非守护线程已经结束或者退出的时候，即使还有守护线程在运行，进程仍然将结束。

> 终止线程？

Java没有提供任何机制来安全地终止线程，那么怎么使线程停止或者中断呢？

主要有三种：

1、线程自己在run()方法执行完后自动终止

2、调用Thread.stop()方法强迫停止一个线程，不过此方法是不安全的，已经不再建议使用。

![img](https://upload-images.jianshu.io/upload_images/2243690-39f63817a626554e.png?imageMogr2/auto-orient/strip|imageView2/2/w/335/format/webp)

3、比较安全可靠的是利用Java的中断机制，使用方法Thread.interrupt()。需要注意的是，通过中断并不能直接终止另一个线程，需要被中断的线程自己处理中断。被终止的线程一定要添加代码对isInterrupted状态进行处理，否则即使代码是死循环的情况下，线程也将永远不会结束。

### 四、线程安全

> 多线程问题

为了继续下面的内容，首先我们看下面的代码，并运行查看结果。



```csharp
/**
 * @time 2016年9月14日 上午10:52:32
 */
public class TestThread {

    public static void main(String[] args) {

        ClassAdd add = new ClassAdd();

        for (int i = 0; i < 5; i++) {
            //开启5个新的线程并启动
            new NewThread(add).start();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("最后的值为" + add.num);
    }
}

//线程类NewThread 对数字进行操作
class NewThread extends Thread {

    private ClassAdd classAdd;

    public NewThread(ClassAdd classAdd) {
        this.classAdd = classAdd;
    }

    @Override
    public void run() {
        classAdd.add();
    }

}

//类ClassAdd 给数字加1
class ClassAdd {

    public int num = 0;

    public void add() {

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        num += 1;
    }
}
```

上面的代码中，我们开启5个线程，每个线程都是对数字进行加1操作，按理说最后结果应该是5,但是实际运行时我们发现结果并不确定，有时为3，有时为4。为什么会出现这样的结果呢，这就要了解一下JMM了。

> JMM

即Java内存模型，它规定了JVM有主内存和工作内存之分，主内存存放程序中的所有类的实例、静态数据等变量，是多个线程共享的，而工作内存存放的是线程从主内存中拷贝过来的变量以及访问方法所取得的变量。是每个线程私有的。每个线程对变量的操作都是先从主内存将其拷贝到工作内存再对其进行操作。由JMM可以知道，(1) 单个线程与线程之间有相互隔离的效果，即可见性问题。(2) 线程与线程之间存在操作的先后顺序，先后顺序将会决定最终对主内存的修改是不是正确的，这是时序性问题。

![img](https://upload-images.jianshu.io/upload_images/2243690-a5c4a69d8880669b.png?imageMogr2/auto-orient/strip|imageView2/2/w/579/format/webp)

JMM

到这里，我们应该知道上面的代码为什么会出现结果不确定的原因了。什么是线程安全呢，就是当多个线程访问一个对象时，如果不用考虑这些线程在运行时的调度和交替执行，也不需要进行额外的同步，或者在调用方进行任何其它的协调操作，调用这个对象的行为都可以获得正确的结果。为了保证线程安全，我们有必要对常见的锁机制有所了解。

> 常见锁机制

- **synchronized 同步锁**

synchronized，是Java里面的一个关键词，当它用来修饰一个方法或者一个代码块的时候，能够保证在同一时刻最多只有一个线程执行该段代码。

synchronized的常用写法如下



```java
写法一、修饰在方法上

    public synchronized void add1() {

    }

写法二、修饰在代码块上

    public void add2() {

        synchronized (this) {

        }
    }

写法三、指定一个小的对象值进行加锁

    private byte[] lock = new byte[1];

    public void add3() {
        synchronized (lock) {

        }
    }
```

上面synchronized三种写法中，最后一种性能和执行效率最高，synchronized修饰方法上的效率最低。原因主要是作用在方法体上的话，即使获得了锁那么进入方法体内分配资源还是需要一定时间的。前两种锁的对象都是对象本身，加锁和释放锁都需要此对象的资源，那么自己造一个byte对象，可以提升效率。

关于synchronized的使用，其实很多地方可以看到，Java2推出的集合框架中大多是非线程安全的，而之前因为效率问题不建议使用的Vector、Hashtable等类却是线程安全的，查看源码可以知道，几个旧的容器类在涉及到元素更新等操作的方法上都加了synchronized关键词，保证同步。

- **ReentrantLock**

在介绍ReentrantLock之前，我们先看一个接口Lock。对于这个接口，官方介绍如下：

> Lock implementations provide more extensive locking operations than can be obtained using synchronized methods and statements. They allow more flexible structuring, may have quite different properties, and may support multiple associated Condition objects.

简单的说，Lock就是提供比synchronized更好的锁操作。相对而言，它比synchronized更灵活，但是必须手动释放和开启锁，适用于代码块锁，synchronized对象之间是互斥关系。

ReentrantLock是接口Lock的一个具体实现类。当许多线程视图访问ReentrantLock保护的共享资源时，JVM将花费较少的时间来调度线程，用更多的时间执行线程。它的用法主要如下：



```csharp
 class X {
  
    private final ReentrantLock lock = new ReentrantLock();
 
    public void m() {

      lock.lock();  // 获得锁

      try {
        ... //方法体
      } finally {

        lock.unlock()

      }
    }
  }
 }
```

在Java的并发编程方面，还有很多其它的锁机制，更多的还是要在实践中去总结，这里暂时就详细介绍这两种锁机制，简单小结一下。

> synchronized是在JVM层面实现的锁，可以通过一些监控工具监控synchronized的锁定，当代码执行时出现异常，JVM会自动释放锁定。当只有少量竞争者时，synchronized是一个很好的通用锁实现，它是针对一个对象的。ReentrantLock使用于比较简单的加锁、解锁的业务逻辑，如果实现复杂的锁机制，当线程增长能够预估时也是可以的。另外，还有ReentrantReadWriteLock和JDK 1.8中推出的StampedLock，ReentrantReadWriteLock是对ReentrantLock的复杂扩展，能适合更加复杂的业务场景，它可以实现一个方法中读写分离的锁的机制，并发性更高。此外，StampedLock在Lock的基础上，实现了满足乐观锁和悲观锁等一些在读线程越来越多的业务场景，对吞吐量有巨大的改进。

最后，再简单提一下，volatile是Java 语言提供了一种稍弱的同步机制，用来确保将变量的更新操作通知到其他线程，保证了新值能立即同步到主内存，以及每次使用前立即从主内存刷新。当把变量声明为volatile类型后，编译器与运行时都会注意到这个变量是共享的。需要注意的是，volatile只提供了内存可见性，没有提供原子性。更多关于volatile的介绍，http://www.cnblogs.com/dolphin0520/p/3920373.html

### 五、线程池

**1.概述**

**什么是线程池？**

在面向对象编程中，创建和销毁对象是很费时间的，因为创建一个对象要获取内存资源或者其它更多资源。在Java中更是如此，JVM将试图跟踪每个对象，以便能够在对象销毁后进行垃圾回收。Java线程池实现了一个Java高并发的、多线程的、可管理的统一调度器，减少创建和销毁线程对象的次数。

**线程池的好处？**

(1) 降低资源消耗，通过重用已经创建的线程，降低线程创建和销毁造成的消耗。 (2) 提高响应速度，当任务到达时，任务可以不需要等到线程创建就能立即执行。 (3) 提高线程的可管理性，避免无限制创建线程，使用线程池可以进行统一的分配、调优和监控。

**2.实例**



```java
public class Main {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 3; i++) {
            final int j = i;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("aaa" + j);
                }
            });
        }
        executorService.shutdown();
    }
}
```

上面的例子中，我们用类Executors的方法newCachedThreadPool()生成了一个ExecutorService实例，然后调用ExecutorService的方法execute()来执行打印任务。

方法newCachedThreadPool创建出的线程池是可根据需要伸缩的线程池，但是之前构造的线程如果可以重用那么就重用，如果没有线程可以重用，就创建一个新线程并添加到池中，缓存的线程保留时间是60s，到时会被移除。

newCachedThreadPool方法可以很方便地替换成另外两个方法，创建出不同类型的线程池。

- **newSingleThreadExecutor()**

创建一个单线程的线程池，这个线程池只有一个线程在工作，也就是相当于单线程串行执行所有任务。如果这个唯一的线程因为异常结束，那么会有一个新的线程来替代它。此线程池保证所有任务的执行顺序按照任务的提交顺序执行。

- **newFixedThreadExecutor()**

传入参数nThreads，创建一个可重用固定线程数的线程池，以共享的无界队列方式来运行这些线程。在任意点，最多nThreads个线程会处于处理任务的活动状态。如果在所有线程处于活动状态时提交附加任务，则在有可用线程之前，附加任务将在队列中等待。如果在关闭前的执行期间由于失败而导致任何线程终止，那么一个新线程将代替它执行后续的任务。在某个线程被显示地关闭之前，池中的线程将一直存在。

**3.线程池源码分析**

1）线程池的创建



```cpp
    public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
             Executors.defaultThreadFactory(), defaultHandler);
    }
```

参数介绍：
**corePoolSize** 核心线程数量，除非设置allowCoreThreadTimeOut，否则即使空闲也不会回收
**maximumPoolSize** 线程池中允许存在的最大处理线程数
**keepAliveTime** 非核心线程允许的空闲时间，超过时间会被回收
**unit** 时间单位
**workQueue** 线程等待池，任务提交到线程池中后可以先存放在此

2）执行任务



```java
public void execute(Runnable command) {
    if (command == null)
        throw new NullPointerException();
    int c = ctl.get();
    //如果当前的Worker少于核心线程数 创建新的Worker
    if (workerCountOf(c) < corePoolSize) {
        if (addWorker(command, true))
            return;
        c = ctl.get();
    }
    if (isRunning(c) && workQueue.offer(command)) {
        //二次检查
        int recheck = ctl.get();
        if (! isRunning(recheck) && remove(command))
            reject(command);
        else if (workerCountOf(recheck) == 0)
            addWorker(null, false);
    }
    //如果等待池里放不下了 创建新的Worker并执行
    else if (!addWorker(command, false))
        reject(command);
}
```

需要注意一个类Worker，线程池执行提交任务时是通过它来完成的。

3）总结

线程池对于线程的执行过程可以总结如下：

任务提交到线程池后如果核心线程数还未满，会立即创建新的线程并执行任务。
如果核心线程数已经满了，那么会把任务暂时存放在等待队列里，之后会复用已经创建的核心线程来执行任务。如果等待队列放不下了，那么会创建非核心线程来执行任务。

### 六、总结

到这里，关于并发编程的总结就结束了，Java多线程编程技术还有很多东西要去学习，多多实践才能更好地掌握，本文也只是对一些必须要掌握的内容进行介绍。



51人点赞



[Java开发笔记](https://www.jianshu.com/nb/5036517)