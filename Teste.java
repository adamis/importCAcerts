/**
 * 
 */
package br.com.conversor;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;

/**
 * 
 */
public class Teste {


	public static void main(String[] args) {
		try {
			// URL do site para o qual você deseja obter o certificado
			String url = "https://adamis.com.br/";

			// Conectar à URL
			HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
			connection.connect();

			// Obter o certificado do servidor
			Certificate[] certs = connection.getServerCertificates();

			// Salvar o certificado em um arquivo temporário
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			X509Certificate cert = (X509Certificate) certs[0];
			String fileName = "certificado_temporario.cer";
			try (FileOutputStream fos = new FileOutputStream(fileName)) {
				fos.write(cert.getEncoded());
			}

			// Carregar o keystore (cacerts)
			String cacertsPath = System.getProperty("java.home") + "/lib/security/cacerts";
			System.err.println(""+cacertsPath);
			char[] password = "changeit".toCharArray(); // A senha padrão do cacerts
			KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());

			try (FileInputStream fis = new FileInputStream(cacertsPath);
					BufferedInputStream bis = new BufferedInputStream(fis)) {
				ks.load(bis, password);
			}

			// Adicionar o certificado ao keystore
			ks.setCertificateEntry("adamis.com.br", cert);

			// Salvar o keystore de volta ao arquivo cacerts
			try (FileOutputStream fos = new FileOutputStream(cacertsPath)) {
				ks.store(fos, password);
			}

			System.out.println("Certificado adicionado com sucesso ao cacerts.");


		} catch (Exception e) {
			e.printStackTrace();
		}
	}



}
