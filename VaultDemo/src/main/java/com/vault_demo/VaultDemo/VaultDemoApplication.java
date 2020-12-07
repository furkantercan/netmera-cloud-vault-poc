package com.vault_demo.VaultDemo;

import lombok.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.vault.authentication.ClientAuthentication;
import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.config.AbstractVaultConfiguration;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponseSupport;

@SpringBootApplication
public class VaultDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(VaultDemoApplication.class, args);
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Credential {
		private String username;
		private String password;
	}

	@Service
	@ToString
	@RequiredArgsConstructor
	public class VaultTest {
		private final VaultTemplate vaultTemplate;

		@EventListener(ApplicationReadyEvent.class)
		public void refreshesAfterApplicationReady() {
			testWrite();
			testRead();
		}

		public void testWrite(){
			vaultTemplate.write("kv/myapp", new Credential("username", "password"));
		}

		public void testRead(){
			VaultResponseSupport<Credential> response = vaultTemplate
					.read("kv/myapp", Credential.class);

			final Credential data = response.getData();
			System.out.println(data);
		}
	}

}
