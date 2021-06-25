package dev.soha.course202002.schedule.web.config

import org.mybatis.spring.annotation.MapperScan
import org.springframework.context.annotation.Configuration

@MapperScan("dev.soha.course202002.schedule.web.data")
@Configuration class MybatisConfig