/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.util.Config;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;

public class KubernetesUtils {
    private String kubeConfigPath;

    public KubernetesUtils() {
        this.kubeConfigPath = System.getProperty("user.dir") + File.separator + System.getProperty("confPath") + File.separator + ".kube" + File.separator + "config";
    }

    public KubernetesUtils(String kubeConfigPath) {
        this.kubeConfigPath = kubeConfigPath;
    }

    public CoreV1Api kubernetesApi() {
        try {
            ApiClient client = Config.fromConfig(kubeConfigPath);
            Configuration.setDefaultApiClient(client);
            return new CoreV1Api();
        } catch (IOException ex) {
            throw new RuntimeException("Kube config is not found in the path: " + kubeConfigPath);
        }
    }
}
