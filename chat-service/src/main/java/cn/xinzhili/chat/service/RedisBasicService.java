package cn.xinzhili.chat.service;

import cn.xinzhili.xutils.core.ErrorCode;
import cn.xinzhili.xutils.core.FailureException;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisBasicService {

  @Autowired
  private RedisTemplate<String, String> stringRedisTemplate;

  @Autowired
  private RedisTemplate<String, Integer> integerRedisTemplate;

  @Autowired
  private RedisTemplate<String, Object> redisTemplate;

  public void setString(String key, String value) {
    stringRedisTemplate.opsForValue().set(key, value);
  }

  public String getString(String key) {
    return stringRedisTemplate.opsForValue().get(key);
  }

  private void setStringWithExpire(String key, String value, Long expire, TimeUnit timeUnit) {
    stringRedisTemplate.opsForValue().set(key, value, expire, timeUnit);
  }

  private void setInteger(String key, Integer value) {
    integerRedisTemplate.opsForValue().set(key, value);
  }

  private void setIntegerWithExpire(String key, Integer value, Long expire, TimeUnit timeUnit) {
    integerRedisTemplate.opsForValue().set(key, value, expire, timeUnit);
  }

  public void setKeyExpire(String key, Long expire, TimeUnit timeUnit) {
    stringRedisTemplate.expire(key, expire, timeUnit);
  }

  public boolean keyExist(String key) {
    return stringRedisTemplate.hasKey(key);
  }

  public void setHashValue(String key, String name, Object value) {
    stringRedisTemplate.opsForHash().put(key, name, value);
  }

  public void setHashStringValue(String key, String name, String value) {
    stringRedisTemplate.opsForHash().put(key, name, value);
  }

//  public void setHashValue(String key, Map<String, Integer> values) {
//    stringRedisTemplate.opsForHash().putAll(key, values);
//  }

  public void setHashValue(String key, Map<String, String> values) {
    stringRedisTemplate.opsForHash().putAll(key, values);
  }

  public String getHashStringValue(String key, String name) {
    return (String) stringRedisTemplate.opsForHash().get(key, name);
  }

  public Object getHashValue(String key, String hname) {
    return redisTemplate.opsForHash().get(key, hname);
  }

  public Integer getHashIntegerValue(String key, String name) {
    String count = (String) stringRedisTemplate.opsForHash().get(key, name);
    return Objects.nonNull(count) ? Integer.valueOf(count) : null;
  }

  public void unreadCountPlus(String key, String name, Integer value) {
    if (Objects.isNull(name)) {
      throw new FailureException(ErrorCode.INVALID_PARAMS);
    }
    Integer count = Integer.valueOf(stringRedisTemplate.opsForHash().get(key, name).toString());
    stringRedisTemplate.opsForHash().put(key, name, String.valueOf(count + value));
  }


  public Set<Object> getHashKeys(String key) {
    return stringRedisTemplate.opsForHash().keys(key);
  }


  public void setRedis(String pubKey, String key, Object obj) {
    HashOperations<String, String, Object> hashOperations = redisTemplate
        .opsForHash();
    hashOperations.put(pubKey, key, obj);
    redisTemplate.expire(pubKey, 5, TimeUnit.SECONDS);
  }

  public Object getRedis(String pubKey, String key) {
    HashOperations<String, String, ?> hashOperations = redisTemplate
        .opsForHash();
    return hashOperations.get(pubKey, key);
  }

  public void deleteKey(String key) {
    redisTemplate.delete(key);
  }


}
