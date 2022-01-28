package haroldo.truelayer.pokedex.api;

import haroldo.truelayer.pokedex.entity.PokemonInfo;

public interface PokemonAPI {

	public PokemonInfo getPokemonInfo(String pokemonName);

	public PokemonInfo getPokemonTranslatedInfo(String pokemonName);

}
