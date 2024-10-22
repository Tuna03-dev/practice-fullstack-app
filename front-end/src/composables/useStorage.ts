import { ref } from "vue"
import { useAuthStore } from "@/stores/authStore";
import { projectStorage} from "@/configs/firebase";
import {getDownloadURL, ref as storageRef, uploadBytes} from "firebase/storage";




const useStorage = (name: string) => {
    const authStore = useAuthStore();
    const username = authStore.username;
    const filePath = ref<string>("");
    const url = ref<string>("");
    const error = ref<string>("");

    const uploadFile = async (file: File) => {
        filePath.value = `${name}/${username}/${file.name}`
        const fileRef = storageRef(projectStorage, filePath.value); 
        try{
            await uploadBytes(fileRef, file);
            url.value = await getDownloadURL(fileRef);
            return url
        }catch(error: any){
            console.log(error);
            error.value = error.message;
        }
    } 

    return { error, url, uploadFile, filePath}
}

export default useStorage;