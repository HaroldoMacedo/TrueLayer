package haroldo.truelayer.pokedex.api;

import haroldo.truelayer.pokedex.entity.PokemonInfo;

/**
 * Describe the interface of the actual code that returns Pokemon information.
 * 
 * @author Haroldo
 *
 */
public interface PokemonAPI {

	public PokemonInfo getPokemonInfo(String pokemonName);

	public PokemonInfo getPokemonTranslatedInfo(String pokemonName);

}
