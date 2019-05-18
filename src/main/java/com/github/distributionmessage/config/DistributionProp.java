package com.github.distributionmessage.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "distribution")
@Data
public class DistributionProp {

    private boolean update = true;

    private String hostName;

    private Integer port;

    private String queueManager;

    private String channel;

    private Integer ccsid;

    private String queueName;

    private Integer minConcurrency;

    private Integer maxConcurrency;

    private Map<String, String> dxpidDistribution;

    private Map<String, String> msgtypeDistribution;

    private Map<String, Integer> percentageDistribution;

    //比重总数
    private Integer percentageTotal;

    //队列比例范围，从percentageDistribution转化而来
    private Map<String, Integer[]> queuePercentage;

    private List<String> randomDistribution;

    private String defaultQueue;
}