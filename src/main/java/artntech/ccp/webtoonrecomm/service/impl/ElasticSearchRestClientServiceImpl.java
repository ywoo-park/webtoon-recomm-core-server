package artntech.ccp.webtoonrecomm.service.impl;

import artntech.ccp.webtoonrecomm.dto.WebtoonRes;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ElasticSearchRestClientServiceImpl {

    private final RestHighLevelClient restHighLevelClient;

    public ElasticSearchRestClientServiceImpl(final RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    public List<WebtoonRes> randomSearchRequestHandler(final QueryBuilder queryBuilder,
                                                 final String fieldForSorting,
                                                 final int searchStart,
                                                 final int searchSize,
                                                 final String index) {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(queryBuilder);

        searchSourceBuilder.sort(fieldForSorting, SortOrder.ASC);

        searchSourceBuilder.from(searchStart);
        searchSourceBuilder.size(searchSize);

        // ES에 요청 보내기
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.source(searchSourceBuilder);

        // ES로 부터 데이터 받기
        SearchResponse searchResponse = null;

        try {
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SearchHits searchHits = searchResponse.getHits();

        List<WebtoonRes> webtoonResList = new ArrayList<>();

        searchHits.forEach( hit ->{

            Map<String, Object> hitAsMap = hit.getSourceAsMap();

            final ObjectMapper mapper = new ObjectMapper();
            mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

            // JSON 역직렬화
            final WebtoonRes webtoonRes = mapper.convertValue(hitAsMap, WebtoonRes.class);

            webtoonRes.setScore(hit.getScore());

            webtoonResList.add(webtoonRes);

        });

        return webtoonResList;
    }

    public List<WebtoonRes> searchRequestHandler(final QueryBuilder queryBuilder,
                                                       final int searchStart,
                                                       final int searchSize,
                                                       final String index) {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(queryBuilder);

        searchSourceBuilder.from(searchStart);
        searchSourceBuilder.size(searchSize);

        // ES에 요청 보내기
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.source(searchSourceBuilder);

        // ES로 부터 데이터 받기
        SearchResponse searchResponse = null;

        try {
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SearchHits searchHits = searchResponse.getHits();

        List<WebtoonRes> webtoonResList = new ArrayList<>();

        searchHits.forEach( hit ->{

            Map<String, Object> hitAsMap = hit.getSourceAsMap();

            final ObjectMapper mapper = new ObjectMapper();
            mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

            // JSON 역직렬화
            final WebtoonRes webtoonRes = mapper.convertValue(hitAsMap, WebtoonRes.class);

            webtoonRes.setScore(hit.getScore());

            webtoonResList.add(webtoonRes);

        });

        return webtoonResList;
    }
}