/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nebhale.cla.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.nebhale.cla.github.GitHubRestOperations;

@SessionAttributes({ "userInfo", "hrefPrefix" })
abstract class AbstractController {

    private static final String DEFAULT_SCHEME = "http";

    private static final String SECURE_SCHEME = "https";

    private static final String HEADER_HOST = "host";

    private final GitHubRestOperations gitHubRestOperations;

    protected AbstractController(GitHubRestOperations gitHubRestOperations) {
        this.gitHubRestOperations = gitHubRestOperations;
    }

    @SuppressWarnings("unchecked")
    @ModelAttribute("userInfo")
    final Map<String, Object> userInfo() {
        return this.gitHubRestOperations.getForObject("/user", Map.class);
    }

    @ModelAttribute("hrefPrefix")
    final String getHrefPrefix(HttpServletRequest httpServletRequest) {
        if (httpServletRequest != null) {
            String scheme = getScheme(httpServletRequest);
            String host = httpServletRequest.getHeader(HEADER_HOST);

            return String.format("%s://%s", scheme, host);
        }
        return "";
    }

    private static String getScheme(HttpServletRequest httpServletRequest) {
        return httpServletRequest.isSecure() ? SECURE_SCHEME : DEFAULT_SCHEME;
    }
}
