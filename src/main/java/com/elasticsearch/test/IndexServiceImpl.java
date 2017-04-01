package com.elasticsearch.test;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.log4j.Logger;
import org.elasticsearch.client.Response;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class IndexServiceImpl   {
    protected Logger logger = Logger.getLogger(IndexServiceImpl.class);

    BilinRestClient bilinRestClient =new BilinRestClient();

    public Integer createOrModifyIndex(String index,String type,String id,String docJson){
        try {
            String endpoint=this.generateEndpoint(index,type,id);
            HttpEntity entity = new StringEntity(docJson, ContentType.APPLICATION_JSON);
            bilinRestClient.getRestClient().performRequest("PUT", endpoint, new HashMap(), entity, bilinRestClient.getHeader());
        } catch (Exception e) {
            logger.error(String.format("createOrModifyIndex error with index:%s,type:%s,id:%s",index,type,id,e));
        }
        return 0;
    }


    public String getIndex(String index,String type,String id){
        try {
            String endpoint=this.generateEndpoint(index,type,id);
            Response response=bilinRestClient.performRequest("GET", endpoint);
            return getStringByResponse(response);
        } catch (Exception e) {
            logger.error(String.format("getIndex error with index:%s,type:%s,id:%s",index,type,id,e));
            return null;
        }
    }


    public String searchIndex(String index, String type, SearchRequest searchRequest){
        List<HashMap> filterList=new ArrayList();
        HashMap searchMap=new HashMap();
        HashMap queryMap=new HashMap();
        Map<String,Object> queryTerm=searchRequest.getQueryTerm();
        if(queryTerm!=null&&queryTerm.keySet().size()>0){
            for(Map.Entry entry:queryTerm.entrySet()){
                HashMap filterMap=new HashMap();
                HashMap termhMap=new HashMap();
                termhMap.put(entry.getKey(),queryTerm.get(entry.getKey()));
                filterMap.put("term",termhMap);
                filterList.add(filterMap);
            }
            HashMap boolMap=new HashMap();
            boolMap.put("filter",filterList);
            queryMap.put("bool",boolMap);

        }
        else {
            queryMap.put("match_all", Collections.EMPTY_MAP);
        }
        searchMap.put("query",queryMap);
        if(searchRequest.getOrderByFieldName()!=null){
            HashMap sortMap=new HashMap();
            if(searchRequest.isAsc()){
                sortMap.put(searchRequest.getOrderByFieldName(),"asc");
            }
            else {
                sortMap.put(searchRequest.getOrderByFieldName(),"desc");
            }
            searchMap.put("sort",sortMap);
        }
        if(searchRequest.getFrom()<0){
            searchMap.put("from",0);
        }
        else {
            searchMap.put("from",searchRequest.getFrom());
        }
        if(searchRequest.getSize()==0){
            searchMap.put("size",100);
        }
        else {
            searchMap.put("size",searchRequest.getSize());
        }

        try {
            String doc = JSON.toJSONString(searchMap);
            HttpEntity entity = new StringEntity(doc, ContentType.APPLICATION_JSON);
            String endpoint=this.generateEndpoint(index,type,null);
            Response response=bilinRestClient.performRequest("POST", endpoint, new HashMap(), entity, bilinRestClient.getHeader());
            return getStringByResponse(response);
        } catch (Exception e) {
            logger.error(String.format("searchIndex error with index:%s,type:%s",index,type,e));
            return null;
        }
    }


    public String searchIndex(String index, String type, String entityJson){
        try {
            HttpEntity entity = new StringEntity(entityJson, ContentType.APPLICATION_JSON);
            String endpoint=this.generateEndpoint(index,type,null);
            Response response=bilinRestClient.performRequest("POST", endpoint, new HashMap(), entity, bilinRestClient.getHeader());
            return getStringByResponse(response);
        } catch (Exception e) {
            logger.error(String.format("searchIndex error with index:%s,type:%s",index,type,e));
            return null;
        }
    }


    public Integer deleteIndex(String index,String type,String id){
        try {
            String endpoint=this.generateEndpoint(index,type,id);
            bilinRestClient.performRequest("DELETE", endpoint);
        } catch (Exception e) {
            logger.error(String.format("deleteIndex error with index:%s,type:%s,id:%s",index,type,id,e));
        }
        return 0;
    }


    public Integer deleteIndexByCondition(String index, String type, String conditionJson){
        try {
            String endpoint=this.generateEndpoint(index,type,null);
            HttpEntity entity = new StringEntity(conditionJson, ContentType.APPLICATION_JSON);
            bilinRestClient.performRequest("DELETE", endpoint, new HashMap(), entity, bilinRestClient.getHeader());
            return  1;
        } catch (Exception e) {
            logger.error(String.format("deleteIndexByCondition error with index:%s,type:%s",index,type,e));
            return null;
        }
    }

    private    String getStringByResponse(Response response)throws Exception{
        if(response == null){
            throw new Exception();
        }
        InputStream is = response.getEntity().getContent();
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) !=null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

    private String generateEndpoint(String index,String type,String id)throws Exception{
        StringBuilder endpoint=new StringBuilder();
        if(index!=null||type!=null){
            throw new Exception();
        }
        else {
            return id!=null?endpoint.append(index).append("/").append(type) .append("/").append(id).toString():endpoint .append(index).append("/").append(type).append("/").append("_search").toString();
        }
    }
}
