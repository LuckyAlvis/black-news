package com.shenzhen.dai;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/* kafka启动脚本
docker network create kafka-net
docker run -d --name zookeeper --network kafka-net -p 12181:2181 wurstmeister/zookeeper
docker run -d --name kafka --network kafka-net -p 9092:9092 -e KAFKA_BROKER_ID=0 -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 \
-e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 -e KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9092 -e TZ="Asia/Shanghai" wurstmeister/kafka
 */
@Slf4j
public class ProducerQuickStart {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 1.kafka配置信息
        Properties prop = new Properties();
        // kafka链接地址
        prop.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        // 重试次数
        prop.put(ProducerConfig.RETRIES_CONFIG, 5);
        //数据压缩
        prop.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "lz4");
        // key和value的序列化
        prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");


        // 2.创建kafka生产者对象
        KafkaProducer<String, String> producer = new KafkaProducer<>(prop);
        for (int i = 0; i < 10; i++) {
            ProducerRecord<String, String> record = new ProducerRecord<>("topic-black", "100001", "hello-kafka,hahaha" + i);
            producer.send(record);
        }
        producer.close();
    }
}

