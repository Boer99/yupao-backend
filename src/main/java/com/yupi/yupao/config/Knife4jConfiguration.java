package com.yupi.yupao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@EnableSwagger2WebMvc
//@Profile("dev") // 该Bean只在开发环境生效
//@Profile("prod")  // 该Bean只在生产环境生效
public class Knife4jConfiguration {

    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                //分组名称
                .groupName("2.X版本")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.yupi.yupao.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    /**
     * api信息
     *
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("yupao swagger-bootstrap-ui-demo RESTful APIs")
                .description("yupao的API文档")
                .termsOfServiceUrl("http://www.xx.com/")
                .contact("xx@qq.com")
                .version("1.0")
                .build();
    }
}