package com.api.spring.scan.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.api.spring.scan")
public class ScanConfig {
}
