package com.example.demo.rpc;

import com.example.demo.dto.RpcServerDto;
import com.example.demo.netty.NettyClient;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pikaqiu
 */
@Component
public class RpcServerPool implements ApplicationListener<ApplicationStartedEvent> {
    static Logger log = LoggerFactory.getLogger(RpcClient.class.getName());
    private final Map<String, RpcServerDto> serverDtoMap = new HashMap<>();

    private Map<String, List<NettyClient>> channelMap = new HashMap<>();

    /**
     * 初始化所有连接
     */
    public void initAllConnect() {

        RpcServerBuild rpcServerPoolBuild = new RpcServerBuild();
        rpcServerPoolBuild.serverAdd("user", "127.0.0.1", 7001);

        for (String serverName : serverDtoMap.keySet()) {
            RpcServerDto rpcServerDto = serverDtoMap.get(serverName);
            for (RpcServerDto.Example example : rpcServerDto.getExamples()) {
                //循环创建连接
                log.info("创建连接 服务: {}：ip: {} ,port: {}", serverName, example.getIp(), example.getPort());

                NettyClient nettyClient = new NettyClient();
                nettyClient.initClient().createConnect(1, example.getIp(), example.getPort());

                List<NettyClient> nettyClients = channelMap.get(serverName);

                if (nettyClients == null) {
                    nettyClients = new ArrayList<>();
                    channelMap.put(serverName, nettyClients);
                }
                nettyClients.add(nettyClient);
            }
        }
    }

    /**
     * 获取一个连接
     *
     * @return
     */
    public ChannelFuture getChannelByServerName(String serverName) {
        //随机获取一个连接
        List<NettyClient> nettyClients = channelMap.get(serverName);
        ChannelFuture channelFuture = null;
        for (; nettyClients.size() > 0; ) {
            channelFuture = nettyClients.get((int) (Math.random() * (nettyClients.size()))).getChannelFuture();

            if (channelFuture != null && channelFuture.channel().isActive()) {
                break;
            } else {
                nettyClients.remove(channelFuture);
            }
        }
        return channelFuture;
    }

    public class RpcServerBuild {

        /**
         * 添加服务名和参数
         * @param serverName
         * @param ip
         * @param port
         * @return
         */
        public RpcServerBuild serverAdd(String serverName, String ip, int port) {
            RpcServerDto serverDto = serverDtoMap.get(serverName);

            if (serverDto == null) {
                serverDto = new RpcServerDto(serverName);
            }
            serverDto.addExample(ip, port);
            serverDtoMap.put(serverName, serverDto);

            return this;
        }

    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationEvent) {
        initAllConnect();
    }

}
