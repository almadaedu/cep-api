package com.linx.correios.repository;

import com.linx.correios.model.Address;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Repository
public class SetupRepository {

    @Value(("${correios.base.url}"))
    private String url;

    private List<Address> resultList = new ArrayList<>();

    public List<Address> getfromOrigin() throws Exception{
        String resultStr = "";

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(new HttpGet(url))) {

            HttpEntity entity = response.getEntity();
            resultStr = EntityUtils.toString(entity);
        }

        String[] resultStrSplited = resultStr.split("\n");

        for(String currentLine : resultStrSplited) {
            String[] currentLineSplited = currentLine.split(",");
            resultList.add(Address.builder()
                            .state(currentLineSplited[0])
                            .city(currentLineSplited[1])
                            .district(currentLineSplited[2])
                            .zipCode(StringUtils.leftPad(currentLineSplited[3], 8, "0"))
                            .street(currentLineSplited.length > 4 ? currentLineSplited[4] : null)
                            .build());


        }

        return resultList;
    }
}
