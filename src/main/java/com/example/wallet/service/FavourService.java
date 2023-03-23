package com.example.wallet.service;

import com.example.wallet.dto.FavourRequest;
import com.example.wallet.entity.Favour;

public interface FavourService {

    public Favour getByIdFavor(int id);

    public String createFavour(FavourRequest favourRequest);
}
