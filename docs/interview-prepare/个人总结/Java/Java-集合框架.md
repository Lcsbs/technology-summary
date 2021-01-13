# Java - 集合框架完全解析

[尘语凡心](https://www.jianshu.com/u/a82533b0437e)关注

42016.09.14 15:56:52字数 3,862阅读 40,982

数据结构是以某种形式将数据组织在一起的集合，它不仅存储数据，还支持访问和处理数据的操作。Java提供了几个能有效地组织和操作数据的数据结构，这些数据结构通常称为Java集合框架。在平常的学习开发中，灵活熟练地使用这些集合框架，可以很明显地提高我们的开发效率，当然仅仅会用还是不够的，理解其中的设计思想与原理才能更好地提高我们的开发水平。下面是自己对Java集合框架方面的学习总结。



```css
一、概述
二、Collection接口
   1.List
   2.Set
   3.Queue
三、Map接口
   1.HashMap实现原理
   2.其它Map实现类
四、其它集合类
五、总结
```

### 一、概述

在Java 2之前，Java是没有完整的集合框架的。它只有一些简单的可以自扩展的容器类，比如Vector，Stack，Hashtable等。这些容器类在使用的过程中由于效率问题饱受诟病，因此在Java 2中，Java设计者们进行了大刀阔斧的整改，重新设计，于是就有了现在的集合框架。需要注意的是，之前的那些容器类库并没有被弃用而是进行了保留，主要是为了向下兼容的目的，但我们在平时使用中还是应该尽量少用。

![img](https://upload-images.jianshu.io/upload_images/2243690-9cd9c896e0d512ed.gif?imageMogr2/auto-orient/strip|imageView2/2/w/643/format/webp)

Java集合框架

从上面的集合框架图可以看到，Java集合框架主要包括两种类型的容器，一种是集合（Collection），存储一个元素集合，另一种是图（Map），存储键/值对映射。Collection接口又有3种子类型，List、Set和Queue，再下面是一些抽象类，最后是具体实现类，常用的有ArrayList、LinkedList、HashSet、LinkedHashSet、HashMap、LinkedHashMap等等。

### 二、Collection接口

Collection接口是处理对象集合的根接口，其中定义了很多对元素进行操作的方法，AbstractCollection是提供Collection部分实现的抽象类。下图展示了Collection接口中的全部方法。

![img](https://upload-images.jianshu.io/upload_images/2243690-52a75aed9de21b68.png?imageMogr2/auto-orient/strip|imageView2/2/w/475/format/webp)

Collection接口结构

其中，有几个比较常用的方法，比如方法add()添加一个元素到集合中，addAll()将指定集合中的所有元素添加到集合中，contains()方法检测集合中是否包含指定的元素，toArray()方法返回一个表示集合的数组。Collection接口有三个子接口，下面详细介绍。

##### 1.List

List接口扩展自Collection，它可以定义一个允许重复的有序集合，从List接口中的方法来看，List接口主要是增加了面向位置的操作，允许在指定位置上操作元素，同时增加了一个能够双向遍历线性表的新列表迭代器ListIterator。AbstractList类提供了List接口的部分实现，AbstractSequentialList扩展自AbstractList，主要是提供对链表的支持。下面介绍List接口的两个重要的具体实现类，也是我们可能最常用的类，ArrayList和LinkedList。

> ArrayList

通过阅读ArrayList的源码，我们可以很清楚地看到里面的逻辑，它是用数组存储元素的，这个数组可以动态创建，如果元素个数超过了数组的容量，那么就创建一个更大的新数组，并将当前数组中的所有元素都复制到新数组中。假设第一次是集合没有任何元素，下面以插入一个元素为例看看源码的实现。
**1）内部数据结构**



```java
//用来存放数据元素的数组
transient Object[] elementData;
//当前存储元素的个数
private int size;
```

**2）添加指定元素**



```java
public boolean add(E e) {
    ensureCapacityInternal(size + 1);  //扩容处理
    elementData[size++] = e;//添加元素
    return true;
}

//此方法主要是确定将要创建的数组大小。
private void ensureCapacityInternal(int minCapacity) {
    if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
        minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
    }
    ensureExplicitCapacity(minCapacity);
}

private void ensureExplicitCapacity(int minCapacity) {
    modCount++;
    if (minCapacity - elementData.length > 0)
        grow(minCapacity);
}

//确定了添加元素后的大小之后将元素复制到新数组中
private void grow(int minCapacity) {
    int oldCapacity = elementData.length;
    int newCapacity = oldCapacity + (oldCapacity >> 1);//扩容后的容量增加一半
    if (newCapacity - minCapacity < 0)
        newCapacity = minCapacity;
    if (newCapacity - MAX_ARRAY_SIZE > 0)
       newCapacity = hugeCapacity(minCapacity);
    // minCapacity is usually close to size, so this is a win:
    elementData = Arrays.copyOf(elementData, newCapacity);
}
```

**3）移除指定元素**



```csharp
public boolean remove(Object o) {
    if (o == null) {
        for (int index = 0; index < size; index++)
            if (elementData[index] == null) {
                fastRemove(index);
                return true;
            }
    } else {
        for (int index = 0; index < size; index++)
            if (o.equals(elementData[index])) {
                fastRemove(index);
                return true;
            }
    }
    return false;
}
    
private void fastRemove(int index) {
    modCount++;
    int numMoved = size - index - 1;
    if (numMoved > 0) // 删除某个元素时需要移动其它数组元素
        System.arraycopy(elementData, index+1, elementData, index, numMoved);
    elementData[--size] = null; // clear to let GC do its work
}
```

> LinkedList

同样，我们打开LinkedList的源文件，不难看到LinkedList是在一个链表中存储元素。所以，LinkedList的元素添加和删除其实就对应着链表节点的添加和移除。



```java
//存储的元素个数
transient int size = 0;
//头节点
transient Node<E> first;
//尾节点
transient Node<E> last;
```

**1）添加元素**



```java
public boolean add(E e) {
    linkLast(e);
    return true;
}

void linkLast(E e) {
    final Node<E> l = last;
    final Node<E> newNode = new Node<>(l, e, null);
    last = newNode;
    if (l == null)
        first = newNode;
    else
        l.next = newNode;
    size++;
    modCount++;
}
```

**2）删除元素**



```java
E unlink(Node<E> x) {
    final E element = x.item;
    final Node<E> next = x.next;
    final Node<E> prev = x.prev;

    if (prev == null) {// 当前节点为头节点，重置头节点
        first = next;
    } else {//当前节点非头节点，将前驱节点和后继节点连接
        prev.next = next;
        x.prev = null;
    }

    if (next == null) {//当前节点为尾节点，重置尾节点
        last = prev;
    } else {// 当前节点非尾节点
        next.prev = prev;
        x.next = null;
    }

    x.item = null;
    size--;
    modCount++;
    return element;
}
```

在学习数据结构的时候，我们知道链表和数组的最大区别在于它们对元素的存储方式的不同导致它们在对数据进行不同操作时的效率不同，同样，ArrayList与LinkedList也是如此，实际使用中我们需要根据特定的需求选用合适的类，如果除了在末尾外不能在其他位置插入或者删除元素，那么ArrayList效率更高，如果需要经常插入或者删除元素，就选择LinkedList。

##### 2.Set

Set接口扩展自Collection，它与List的不同之处在于，规定Set的实例不包含重复的元素。AbstractSet是一个实现Set接口的抽象类，Set接口有三个具体实现类，分别是散列集HashSet、链式散列集LinkedHashSet和树形集TreeSet。

> 散列集HashSet

散列集HashSet是一个用于实现Set接口的具体类，可以使用它的无参构造方法来创建空的散列集，也可以由一个现有的集合创建散列集。在散列集中，有两个名词需要关注，初始容量和客座率。实际上HashSet就是基于后面介绍的HashMap而实现的，客座率是确定在增加规则集之前，该规则集的饱满程度，当元素个数超过了容量与客座率的乘积时，容量就会自动翻倍。

下面看一个HashSet的例子。



```csharp
public class TestHashSet {
    public static void main(String[] args) {
        Set<String> set = new HashSet<>();
        set.add("11111");
        set.add("22222");
        set.add("33333");
        set.add("44444");
        set.add("22222");
        System.out.println(set.size());
        for (String e : set) {
            System.out.println(e);
        }
    }
}
```

从输出结果我们可以看到，规则集里最后有4个元素，而且在输出时元素还是无序的。

查看散列集HashSet的源码实现可以看到它内部是使用一个HashMap来存放元素的，因为HashSet的元素就是其内部HashMap的**键集合**，所以所以HashSet可以做到元素不重复。

> 链式散列集LinkedHashSet

LinkedHashSet是继承自HashSet的，支持对规则集内的元素排序。HashSet中的元素是没有被排序的，而LinkedHashSet中的元素可以按照它们插入规则集的顺序提取。

> 树形集TreeSet

TreeSet扩展自AbstractSet，并实现了NavigableSet，AbstractSet扩展自AbstractCollection，树形集是一个有序的Set，其底层是一颗树，这样就能从Set里面提取一个有序序列了。在实例化TreeSet时，我们可以给TreeSet指定一个比较器Comparator来指定树形集中的元素顺序。树形集中提供了很多便捷的方法。

下面是一个TreeSet的例子。



```csharp
/**
 * @author JackalTsc
 */
public class TestSet {

    public static void main(String[] args) {

        TreeSet<Integer> set = new TreeSet<>();

        set.add(1111);
        set.add(2222);
        set.add(3333);
        set.add(4444);
        set.add(5555);

        System.out.println(set.first()); // 输出第一个元素
        System.out.println(set.lower(3333)); //小于3333的最大元素
        System.out.println(set.higher(2222)); //大于2222的最大元素
        System.out.println(set.floor(3333)); //不大于3333的最大元素
        System.out.println(set.ceiling(3333)); //不小于3333的最大元素

        System.out.println(set.pollFirst()); //删除第一个元素
        System.out.println(set.pollLast()); //删除最后一个元素
        System.out.println(set);
    }
}
```

##### 3.Queue

队列是一种先进先出的数据结构，元素在队列末尾添加，在队列头部删除。Queue接口扩展自Collection，并提供插入、提取、检验等操作。

![img](https://upload-images.jianshu.io/upload_images/2243690-05355ad0eda78e6e.png?imageMogr2/auto-orient/strip|imageView2/2/w/325/format/webp)

Queue接口结构

上图中，方法offer表示向队列添加一个元素，poll()与remove()方法都是移除队列头部的元素，两者的区别在于如果队列为空，那么poll()返回的是null，而remove()会抛出一个异常。方法element()与peek()主要是获取头部元素，不删除。

接口Deque，是一个扩展自Queue的双端队列，它支持在两端插入和删除元素，因为LinkedList类实现了Deque接口，所以通常我们可以使用LinkedList来创建一个队列。PriorityQueue类实现了一个优先队列，优先队列中元素被赋予优先级，拥有高优先级的先被删除。



```cpp
/**
 * @author JackalTsc
 */
public class TestQueue {

    public static void main(String[] args) {

        Queue<String> queue = new LinkedList<>();

        queue.offer("aaaa");
        queue.offer("bbbb");
        queue.offer("cccc");
        queue.offer("dddd");

        while (queue.size() > 0) {
            System.out.println(queue.remove() + "");
        }
    }
}
```

### 三、Map接口

Map，图，是一种存储键值对映射的容器类，在Map中键可以是任意类型的对象，但不能有重复的键，每个键都对应一个值，真正存储在图中的是键值构成的条目。下面是接口Map的类结构。

![img](https://upload-images.jianshu.io/upload_images/2243690-78354bae85e2dc3c.png?imageMogr2/auto-orient/strip|imageView2/2/w/564/format/webp)

接口Map的结构

从上面这张图中我们可以看到接口Map提供了很多查询、更新和获取存储的键值对的方法，更新包括方法clear()、put()、putAll()、remove()等等，查询方法包括containsKey、containsValue等等。Map接口常用的有三个具体实现类，分别是HashMap、LinkedHashMap、TreeMap。

##### 1.HashMap

HashMap是基于哈希表的Map接口的非同步实现，继承自AbstractMap，AbstractMap是部分实现Map接口的抽象类。在平时的开发中，HashMap的使用还是比较多的。我们知道ArrayList主要是用数组来存储元素的，LinkedList是用链表来存储的，那么HashMap的实现原理是什么呢？先看下面这张图：

![img](https://upload-images.jianshu.io/upload_images/2243690-ec848d862c725e8d.jpg?imageMogr2/auto-orient/strip|imageView2/2/w/515/format/webp)

HashMap原理.jpg

在之前的版本中，HashMap采用数组+链表实现，即使用链表处理冲突，同一hash值的链表都存储在一个链表里。但是当链表中的元素较多，即hash值相等的元素较多时，通过key值依次查找的效率较低。而JDK1.8中，HashMap采用数组+链表+红黑树实现，当链表长度超过阈值（8）时，将链表转换为红黑树，这样大大减少了查找时间。

下面主要通过源码介绍一下它的实现原理。

> HashMap存储元素的数组



```css
  transient Node<K,V>[] table;
```

> 数组的元素类型是Node<K,V>，Node<K,V>继承自Map.Entry<K,V>，表示键值对映射。



```java
static class Node<K,V> implements Map.Entry<K,V> {
        final int hash;
        final K key;
        V value;
        Node<K,V> next;

        //构造函数 ( Hash值键值下一个节点 )
        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final K getKey()        { return key; }
        public final V getValue()      { return value; }
        public final String toString() { return key + "=" + value; }

        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        public final boolean equals(Object o) {
            if (o == this)
                return true;
            if (o instanceof Map.Entry) {
                Map.Entry<?,?> e = (Map.Entry<?,?>)o;
                if (Objects.equals(key, e.getKey()) &&
                    Objects.equals(value, e.getValue()))
                    return true;
            }
            return false;
        }
    }
```

> 接下来我们看下HashMap的put操作。



```csharp
final V putVal(int hash, K key, V value, boolean onlyIfAbsent, boolean evict) {
    Node<K,V>[] tab; Node<K,V> p; int n, i;
    if ((tab = table) == null || (n = tab.length) == 0)
        n = (tab = resize()).length; //如果没有初始化则初始化table
    if ((p = tab[i = (n - 1) & hash]) == null)
        //根据hash值得到这个元素在数组中的位置 如果此位置还没别的元素 直接存放
        tab[i] = newNode(hash, key, value, null);
    else {
        Node<K,V> e; K k;
        //应该放在首节点的位置
        if (p.hash == hash &&
            ((k = p.key) == key || (key != null && key.equals(k))))
            e = p;
        //红黑树插入新节点
        else if (p instanceof TreeNode)
            e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
        else {
            //链表插入新节点
            for (int binCount = 0; ; ++binCount) {
                if ((e = p.next) == null) {
                    p.next = newNode(hash, key, value, null);
                    //超过阈值 将链表转化为红黑树
                    if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                        treeifyBin(tab, hash);
                    break;
                }
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    break;
                p = e;
            }
        }
        //更新hash值和key值均相同的节点Value值
        if (e != null) { // existing mapping for key
            V oldValue = e.value;
            if (!onlyIfAbsent || oldValue == null)
                e.value = value;
            afterNodeAccess(e);
            return oldValue;
        }
    }
    ++modCount;
    if (++size > threshold)
        resize();
    afterNodeInsertion(evict);
    return null;
}
```

> 接下来我们看下HashMap的get操作。



```csharp
final Node<K,V> getNode(int hash, Object key) {
    Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
    //如果当前Map里还没元素或者hash值对应的链表头节点为空 直接返回null
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (first = tab[(n - 1) & hash]) != null) {
        //是头节点 直接返回
        if (first.hash == hash && // always check first node
            ((k = first.key) == key || (key != null && key.equals(k))))
            return first;
        //非头节点 遍历找到对应key的值
        if ((e = first.next) != null) {
            if (first instanceof TreeNode)
                return ((TreeNode<K,V>)first).getTreeNode(hash, key);
            do {
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    return e;
            } while ((e = e.next) != null);
        }
    }
    return null;
}
```

到这里HashMap的大致实现原理应该很清楚了，有几个需要关注的重点是：HashMap存储元素的方式以及根据Hash值确定映射在数组中的位置还有JDK 1.8之后加入的红黑树的。

> 在HashMap中要找到某个元素，需要根据key的hash值来求得对应数组中的位置。对于任意给定的对象，只要它的hashCode()返回值相同，那么程序调用hash(int h)方法所计算得到的hash码值总是相同的。我们首先想到的就是把hash值对数组长度取模运算，这样一来，元素的分布相对来说是比较均匀的。但是，“模”运算的消耗还是比较大的，在HashMap中，**(n - 1) & hash**用于计算对象应该保存在table数组的哪个索引处。HashMap底层数组的长度总是2的n次方，当数组长度为2的n次幂的时候，**(n - 1) & hash** 算得的index相同的几率较小，数据在数组上分布就比较均匀，也就是说碰撞的几率小，相对的，查询的时候就不用遍历某个位置上的链表，这样查询效率也就较高了。

推荐一篇文章，HashMap源码分析比较详细：[JDK1.8 HashMap源码分析](https://www.cnblogs.com/xiaoxi/p/7233201.html)

##### 2.LinkedHashMap

LinkedHashMap继承自HashMap，它主要是用链表实现来扩展HashMap类，HashMap中条目是没有顺序的，但是在LinkedHashMap中元素既可以按照它们插入图的顺序排序，也可以按它们最后一次被访问的顺序排序。

##### 3.TreeMap

TreeMap基于红黑树数据结构的实现，键值可以使用Comparable或Comparator接口来排序。TreeMap继承自AbstractMap，同时实现了接口NavigableMap，而接口NavigableMap则继承自SortedMap。SortedMap是Map的子接口，使用它可以确保图中的条目是排好序的。

在实际使用中，如果更新图时不需要保持图中元素的顺序，就使用HashMap，如果需要保持图中元素的插入顺序或者访问顺序，就使用LinkedHashMap，如果需要使图按照键值排序，就使用TreeMap。

### 四、其它集合类

上面主要对Java集合框架作了详细的介绍，包括Collection和Map两个接口及它们的抽象类和常用的具体实现类，下面主要介绍一下其它几个特殊的集合类，Vector、Stack、HashTable、ConcurrentHashMap以及CopyOnWriteArrayList。

##### 1.Vector

前面我们已经提到，Java设计者们在对之前的容器类进行重新设计时保留了一些数据结构，其中就有Vector。用法上，Vector与ArrayList基本一致，不同之处在于Vector使用了关键字synchronized将访问和修改向量的方法都变成同步的了，所以对于不需要同步的应用程序来说，类ArrayList比类Vector更高效。

##### 2.Stack

Stack，栈类，是Java2之前引入的，继承自类Vector。

##### 3.Hashtable

Hashtable和前面介绍的HashMap很类似，它也是一个散列表，存储的内容是键值对映射，不同之处在于，Hashtable是继承自Dictionary的，Hashtable中的函数都是同步的，这意味着它也是线程安全的，另外，Hashtable中key和value都不可以为null。

上面的三个集合类都是在Java2之前推出的容器类，可以看到，尽管在使用中效率比较低，但是它们都是线程安全的。下面介绍两个特殊的集合类。

##### 4.ConcurrentHashMap

Concurrent，并发，从名字就可以看出来ConcurrentHashMap是HashMap的线程安全版。同HashMap相比，ConcurrentHashMap不仅保证了访问的线程安全性，而且在效率上与HashTable相比，也有较大的提高。关于ConcurrentHashMap的设计，我将会在下一篇关于并发编程的博客中介绍，敬请关注。

##### 5.CopyOnWriteArrayList

CopyOnWriteArrayList，是一个线程安全的List接口的实现，它使用了ReentrantLock锁来保证在并发情况下提供高性能的并发读取。

### 五、总结

到这里，对于Java集合框架的总结就结束了，还有很多集合类没有在这里提到，更多的还是需要大家自己去查去用。通过阅读源码，查阅资料，收获很大。

- Java集合框架主要包括Collection和Map两种类型。其中Collection又有3种子类型，分别是List、Set、Queue。Map中存储的主要是键值对映射。
- 规则集Set中存储的是不重复的元素，线性表中存储可以包括重复的元素，Queue队列描述的是先进先出的数据结构，可以用LinkedList来实现队列。
- 效率上，规则集比线性表更高效。
- ArrayList主要是用数组来存储元素，LinkedList主要是用链表来存储元素，HashMap的底层实现主要是借助数组+链表+红黑树来实现。
- Vector、Hashtable等集合类效率比较低但都是线程安全的。包java.util.concurrent下包含了大量线程安全的集合类，效率上有较大提升。



230人点赞



[Java开发笔记](https://www.jianshu.com/nb/5036517)