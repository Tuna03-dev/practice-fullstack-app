package org.example.exercise_shop.Service;

import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Repository.SliderRepository;
import org.example.exercise_shop.dto.request.CreateSliderRequest;
import org.example.exercise_shop.entity.Slider;
import org.example.exercise_shop.mapper.SliderMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class SliderServiceImp implements SliderService{

    private final SliderMapper sliderMapper;
    private final SliderRepository sliderRepository;

    @Override
    public void createSlider(CreateSliderRequest createSliderRequest) {
        Slider slider = sliderMapper.toSlider(createSliderRequest);
        sliderRepository.save(slider);
    }

    @Override
    public List<Slider> getSliders() {
        return List.of();
    }

    @Override
    public Slider getSlider(String id) {
        return null;
    }
}
