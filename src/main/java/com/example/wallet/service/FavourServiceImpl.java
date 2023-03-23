package com.example.wallet.service;

import com.example.wallet.dto.FavourRequest;
import com.example.wallet.entity.Favour;
import com.example.wallet.repository.FavourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class FavourServiceImpl implements FavourService{

    private final FavourRepository favourRepository;

    @Autowired
    public FavourServiceImpl(FavourRepository favourRepository) {
        this.favourRepository = favourRepository;
    }

    @Override
    public Favour getByIdFavor(int id) {
        return favourRepository.getByFavourId(id);
    }

    @Override
    public String createFavour(FavourRequest favourRequest) {

        Favour favour = new Favour();

        favour.setTitle(favourRequest.getTitle());
        favour.setDescription(favourRequest.getDescription());

        favourRepository.save(favour);

        return "Favour Created!";
    }
}
