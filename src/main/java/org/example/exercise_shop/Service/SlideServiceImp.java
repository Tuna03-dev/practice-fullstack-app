package org.example.exercise_shop.Service;

import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Repository.SlideRepository;
import org.example.exercise_shop.Repository.SliderRepository;
import org.example.exercise_shop.dto.request.CreateSlideRequest;
import org.example.exercise_shop.dto.response.SlideResponse;
import org.example.exercise_shop.entity.Slide;
import org.example.exercise_shop.entity.Slider;
import org.example.exercise_shop.entity.SliderName;
import org.example.exercise_shop.exception.ApplicationException;
import org.example.exercise_shop.exception.ErrorCode;
import org.example.exercise_shop.mapper.SlideMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SlideServiceImp implements SlideService{

    private final SlideRepository slideRepository;
    private final SlideMapper slideMapper;
    private final SliderRepository sliderRepository;
    @Override
    public void createSlide(CreateSlideRequest createSlideRequest) {
        Slide slide = slideMapper.toSlide(createSlideRequest);
        Slider slider = sliderRepository.findById(createSlideRequest.getSliderId()).orElseThrow(() -> new ApplicationException(ErrorCode.SLIDER_NOT_FOUND));
        slide.setSlider(slider);
        slideRepository.save(slide);
    }

    @Override
    public List<SlideResponse> getSlidesBySliderName(SliderName sliderName) {
        return slideRepository.getAllBySlider_Name(sliderName).stream().map(slideMapper::toSlideResponse).toList();
    }


    @Override
    public Slide getSlide(String id) {
        return null;
    }
}
