import httpClient from "./httpClient";
import { sliderNames } from "../apiTypes";

const SliderApi = {

    getAllSlidesBySliderName: async (sliderName: sliderNames) => {
        return await httpClient.get(`/sliders/get-slides/${sliderName}`)
    },


}

export default SliderApi