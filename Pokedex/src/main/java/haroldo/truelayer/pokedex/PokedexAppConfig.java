package haroldo.truelayer.pokedex;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import haroldo.truelayer.pokedex.api.PokemonAPI;
import haroldo.truelayer.pokedex.api.PokemonAPIImpl;

@Configuration
public class PokedexAppConfig {

	@Bean
	public PokemonAPI pokemonAPI() {
		return new PokemonAPIImpl();
	}

}
