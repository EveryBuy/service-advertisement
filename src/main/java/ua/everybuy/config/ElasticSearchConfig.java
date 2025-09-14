package ua.everybuy.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;

import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
public class ElasticSearchConfig {

    @Value("${elasticsearch.url}")
    private String elasticsearchUrl;

    @Bean(destroyMethod = "close")
    public RestHighLevelClient restHighLevelClient() throws Exception {
        URI uri = new URI(elasticsearchUrl);

        String userInfo = uri.getUserInfo();
        String[] credentials = userInfo.split(":");

        final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(credentials[0], credentials[1]));

        RestClientBuilder builder = RestClient.builder(
                new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme())
        ).setHttpClientConfigCallback(httpClientBuilder ->
                httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
        );

        return new RestHighLevelClient(builder);
    }
}
