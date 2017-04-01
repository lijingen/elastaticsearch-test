package com.elasticsearch.test;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.Map;

/**
 * Created by lijingen on 2017/4/1.
 */
public class BilinRestClient {
    protected  String elasticsearchHost1;
    protected  String elasticsearchHost2;
    protected  int elasticsearchPort;
    protected  String elasticsearchAuth;

    private Header header;
    private RestClient restClient;

    private void init(){
        this.header=new BasicHeader("Authorization", "Basic " + Base64.encodeBase64String(this.elasticsearchAuth.getBytes()));
        this.restClient=RestClient.builder(new HttpHost(this.elasticsearchHost1,this.elasticsearchPort),new HttpHost(this.elasticsearchHost2, this.elasticsearchPort)).setDefaultHeaders(new Header[]{this.header}).build();
    }
    public Header getHeader(){
        return this.header;
    }
    public RestClient getRestClient(){
        return this.restClient;
    }
    public Response performRequest(String method, String endpoint, Map<String, String> params, HttpEntity entity, Header header) throws IOException {
        return this.restClient.performRequest(method, endpoint, params, entity,header);
    }
    public Response performRequest(String method, String endpoint,Header... headers) throws IOException {
        return this.restClient.performRequest(method, endpoint);
    }
    public String getElasticsearchHost2() {
        return elasticsearchHost2;
    }

    public void setElasticsearchHost2(String elasticsearchHost2) {
        this.elasticsearchHost2 = elasticsearchHost2;
    }

    public String getElasticsearchHost1() {
        return elasticsearchHost1;
    }

    public void setElasticsearchHost1(String elasticsearchHost1) {
        this.elasticsearchHost1 = elasticsearchHost1;
    }

    public String getElasticsearchAuth() {
        return elasticsearchAuth;
    }

    public void setElasticsearchAuth(String elasticsearchAuth) {
        this.elasticsearchAuth = elasticsearchAuth;
    }

    public int getElasticsearchPort() {
        return elasticsearchPort;
    }
    public void setElasticsearchPort(int elasticsearchPort) {
        this.elasticsearchPort = elasticsearchPort;
    }
}
