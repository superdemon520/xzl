package cn.xinzhili.api.doctor.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * Created by ywb on 8/6/2017.
 */

/**
 * 管理BlockingQueue Map.
 *
 *
 * @author calvin
 */
@SuppressWarnings("unchecked")
@ManagedResource(objectName = QueueHolder.QUEUE_HOLDER_MBEAN_NAME, description = "Queue Holder Bean")
public class QueueHolder {

  /**
   * QueueManager注册的名称.
   * 暂未使用
   */
  public static final String QUEUE_HOLDER_MBEAN_NAME = "Tfw:type=QueueManagement,name=queueHolder";

  private static ConcurrentMap<String, BlockingQueue> queueMap = new ConcurrentHashMap();//消息队列
  private static int queueSize = Integer.MAX_VALUE;


  public void setQueueSize(int queueSize) {
    QueueHolder.queueSize = queueSize;
  }


  public static <T> BlockingQueue<T> getQueue(String queueName) {
    BlockingQueue queue = queueMap.get(queueName);

    if (queue == null) {
      queue = new LinkedBlockingQueue(queueSize);
      queue = queueMap.putIfAbsent(queueName, queue);
      if (queue == null) {
        return queueMap.get(queueName);
      }
      return queue;
    }
    return queue;
  }


  @ManagedOperation(description = "Get message count in queue")
  @ManagedOperationParameters({
      @ManagedOperationParameter(name = "queueName", description = "Queue name")})
  public static int getQueueLength(String queueName) {
    return getQueue(queueName).size();
  }

}