package cn.xuguowen.infrastructure.persistent.redis;

import org.redisson.api.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Set;
import java.util.function.Predicate;

/**
 * ClassName: RedissonService
 * Package: cn.bugstack.chatgpt.data.infrastructure.redis
 * Description:使用 Redisson 与 Redis 交互的 Redis 服务类
 *
 * @Author 徐国文
 * @Create 2024/5/27 14:53
 * @Version 1.0
 */
@Service("redissonService")
public class RedissonService implements IRedisService {

    @Resource
    private RedissonClient redissonClient;

    /**
     * 将值存储到 Redis 中
     *
     * @param key   存储的键
     * @param value 存储的值
     * @param <T>   值的类型
     */
    public <T> void setValue(String key, T value) {
        redissonClient.<T>getBucket(key).set(value);
    }

    /**
     * 将值存储到 Redis 中，并设置过期时间
     *
     * @param key     存储的键
     * @param value   存储的值
     * @param expired 过期时间（毫秒）
     * @param <T>     值的类型
     */
    @Override
    public <T> void setValue(String key, T value, long expired) {
        RBucket<T> bucket = redissonClient.getBucket(key);
        bucket.set(value, Duration.ofMillis(expired));
    }

    /**
     * 从 Redis 中获取值
     *
     * @param key 存储的键
     * @param <T> 值的类型
     * @return 存储在 Redis 中的值
     */
    public <T> T getValue(String key) {
        return redissonClient.<T>getBucket(key).get();
    }

    @Override
    public <T> Set<T> getSetValues(String key) {
        return redissonClient.<T>getSet(key).readAll();
    }

    @Override
    public <T> void addSetValue(String key, T value) {
        RSet<T> set = redissonClient.getSet(key);
        set.add(value);
    }

    public <T> void removeOldSetValues(String key, Predicate<T> filter) {
        RSet<T> set = redissonClient.getSet(key);
        set.removeIf(filter);
    }

    /**
     * 获取 Redis 中的队列
     *
     * @param key 队列的键
     * @param <T> 队列中元素的类型
     * @return Redis 队列
     */
    @Override
    public <T> RQueue<T> getQueue(String key) {
        return redissonClient.getQueue(key);
    }

    /**
     * 获取 Redis 中的阻塞队列
     *
     * @param key 队列的键
     * @param <T> 队列中元素的类型
     * @return Redis 阻塞队列
     */
    @Override
    public <T> RBlockingQueue<T> getBlockingQueue(String key) {
        return redissonClient.getBlockingQueue(key);
    }

    /**
     * 获取 Redis 中的延迟队列
     *
     * @param rBlockingQueue 阻塞队列
     * @param <T>            队列中元素的类型
     * @return Redis 延迟队列
     */
    @Override
    public <T> RDelayedQueue<T> getDelayedQueue(RBlockingQueue<T> rBlockingQueue) {
        return redissonClient.getDelayedQueue(rBlockingQueue);
    }

    /**
     * 自增键的值
     *
     * @param key 存储的键
     * @return 增加后的值
     */
    @Override
    public long incr(String key) {
        return redissonClient.getAtomicLong(key).incrementAndGet();
    }

    /**
     * 按指定的增量自增键的值
     *
     * @param key   存储的键
     * @param delta 增量
     * @return 增加后的值
     */
    @Override
    public long incrBy(String key, long delta) {
        return redissonClient.getAtomicLong(key).addAndGet(delta);
    }

    /**
     * 自减键的值
     *
     * @param key 存储的键
     * @return 减少后的值
     */
    @Override
    public long decr(String key) {
        return redissonClient.getAtomicLong(key).decrementAndGet();
    }

    /**
     * 按指定的减量自减键的值
     *
     * @param key   存储的键
     * @param delta 减量
     * @return 减少后的值
     */
    @Override
    public long decrBy(String key, long delta) {
        return redissonClient.getAtomicLong(key).addAndGet(-delta);
    }

    /**
     * 删除键
     *
     * @param key 存储的键
     */
    @Override
    public void remove(String key) {
        redissonClient.getBucket(key).delete();
    }

    /**
     * 判断键是否存在
     *
     * @param key 存储的键
     * @return 键是否存在
     */
    @Override
    public boolean isExists(String key) {
        return redissonClient.getBucket(key).isExists();
    }

    /**
     * 向 Redis 集合添加元素
     *
     * @param key   集合的键
     * @param value 要添加的值
     */
    public void addToSet(String key, String value) {
        RSet<String> set = redissonClient.getSet(key);
        set.add(value);
    }

    /**
     * 判断值是否是 Redis 集合的成员
     *
     * @param key   集合的键
     * @param value 要判断的值
     * @return 是否是集合的成员
     */
    public boolean isSetMember(String key, String value) {
        RSet<String> set = redissonClient.getSet(key);
        return set.contains(value);
    }

    /**
     * 向 Redis 列表添加元素
     *
     * @param key   列表的键
     * @param value 要添加的值
     */
    public void addToList(String key, String value) {
        RList<String> list = redissonClient.getList(key);
        list.add(value);
    }

    /**
     * 从 Redis 列表中获取元素
     *
     * @param key   列表的键
     * @param index 元素的索引
     * @return 获取的元素
     */
    public String getFromList(String key, int index) {
        RList<String> list = redissonClient.getList(key);
        return list.get(index);
    }

    /**
     * 获取Map
     *
     * @param key 键
     * @return 值
     */
    public <K, V> RMap<K, V> getMap(String key) {
        return redissonClient.getMap(key);
    }


    /**
     * 向 Redis 映射添加键值对
     *
     * @param key   映射的键
     * @param field 域的键
     * @param value 域的值
     */
    public void addToMap(String key, String field, String value) {
        RMap<String, String> map = redissonClient.getMap(key);
        map.put(field, value);
    }

    /**
     * 从 Redis 映射中获取值
     *
     * @param key   映射的键
     * @param field 域的键
     * @return 获取的值
     */
    public String getFromMap(String key, String field) {
        RMap<String, String> map = redissonClient.getMap(key);
        return map.get(field);
    }

    /**
     * 获取哈希表中指定字段的值
     *
     * @param key   键
     * @param field 字段
     * @return 值
     */
    public <K, V> V getFromMap(String key, K field) {
        return redissonClient.<K, V>getMap(key).get(field);
    }


    /**
     * 向 Redis 有序集合添加元素
     *
     * @param key   有序集合的键
     * @param value 要添加的值
     */
    public void addToSortedSet(String key, String value) {
        RSortedSet<String> sortedSet = redissonClient.getSortedSet(key);
        sortedSet.add(value);
    }

    /**
     * 获取 Redis 锁
     *
     * @param key 锁的键
     * @return Redis 锁
     */
    @Override
    public RLock getLock(String key) {
        return redissonClient.getLock(key);
    }

    /**
     * 获取 Redis 公平锁
     *
     * @param key 锁的键
     * @return Redis 公平锁
     */
    @Override
    public RLock getFairLock(String key) {
        return redissonClient.getFairLock(key);
    }

    /**
     * 获取 Redis 读写锁
     *
     * @param key 锁的键
     * @return Redis 读写锁
     */
    @Override
    public RReadWriteLock getReadWriteLock(String key) {
        return redissonClient.getReadWriteLock(key);
    }

    /**
     * 获取 Redis 信号量
     *
     * @param key 信号量的键
     * @return Redis 信号量
     */
    @Override
    public RSemaphore getSemaphore(String key) {
        return redissonClient.getSemaphore(key);
    }

    /**
     * 获取 Redis 可过期信号量
     *
     * @param key 信号量的键
     * @return Redis 可过期信号量
     */
    @Override
    public RPermitExpirableSemaphore getPermitExpirableSemaphore(String key) {
        return redissonClient.getPermitExpirableSemaphore(key);
    }

    /**
     * 获取 Redis 倒计时锁
     *
     * @param key 倒计时锁的键
     * @return Redis 倒计时锁
     */
    @Override
    public RCountDownLatch getCountDownLatch(String key) {
        return redissonClient.getCountDownLatch(key);
    }

    /**
     * 获取 Redis 布隆过滤器
     *
     * @param key 布隆过滤器的键
     * @param <T> 布隆过滤器中元素的类型
     * @return Redis 布隆过滤器
     */
    @Override
    public <T> RBloomFilter<T> getBloomFilter(String key) {
        return redissonClient.getBloomFilter(key);
    }

    @Override
    public void setAtomicLong(String key, Integer value) {
        redissonClient.getAtomicLong(key).set(value);
    }

    /**
     * sexNx原子性加锁
     * @param key
     * @return
     */
    @Override
    public Boolean setNx(String key) {
        return redissonClient.getBucket(key).setIfAbsent("lock");
    }
}
