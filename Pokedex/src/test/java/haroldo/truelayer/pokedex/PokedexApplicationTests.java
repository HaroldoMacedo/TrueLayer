package haroldo.truelayer.pokedex;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import haroldo.truelayer.pokedex.entity.PokemonInfo;

/**
 * JUnit test of the API.
 * The test is executed by calling the HTTP endpoint and validating the response.
 * 
 * @author Haroldo
 *
 */
@SpringBootTest
class PokedexApplicationTests {
	private static final String BASE_URL = "http://localhost:8080/pokemon";
	private RestTemplate restTemplate = new RestTemplate();

	/**
	 * Get an existing pokemon.
	 */
	@Test
	public void getPokemon() {
		String url = BASE_URL + "/{pokemonname}";
		getPokemonOK(url, "mewtwo");
	}

	/**
	 * Get an existing pokemon to translate.
	 */
	@Test
	public void getTranslatedPokemon() {
		String url = BASE_URL + "/translated/{pokemonname}";
		getPokemonOK(url, "mewtwo");
	}

	/**
	 * Get a pokemon by passing its url and name. Do basic tests on the return
	 * value.
	 * 
	 * @param name
	 */
	private void getPokemonOK(String url, String name) {
		ResponseEntity<PokemonInfo> response = callGetPokemon(url, name);

		assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
		assertEquals(name, response.getBody().getName());
		assertNotNull(response.getBody().getDescription());
		assertNotNull(response.getBody().getHabitat());
		assertNotNull(response.getBody().isLegendary());
	}

	/**
	 * Get nonexisting pokemon.
	 */
	@Test
	public void wrongPokemonName() {
		String url = BASE_URL + "/{pokemonname}";
		String urlTranslated = BASE_URL + "/translated/{pokemonname}";

		getPokemonError(url, "nonexistent");
		getPokemonError(urlTranslated, "nonexistent");
	}

	/**
	 * For test that return errors.
	 * @param url
	 * @param name
	 */
	private void getPokemonError(String url, String name) {
		try {
			ResponseEntity<PokemonInfo> response = callGetPokemon(url, name);
			assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
			assertEquals(response.getBody().getError(), "Pokemon 'nonexistent' not found!");
		} catch (HttpClientErrorException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
		}
	}

	/**
	 * Implement the HTTP call to the API.
	 * 
	 * @param url
	 * @param name
	 * @return
	 */
	private ResponseEntity<PokemonInfo> callGetPokemon(String url, String name) {
		return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>("body"), PokemonInfo.class, name);
	}
}
