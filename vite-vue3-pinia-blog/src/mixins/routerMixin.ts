import { useRouter } from "vue-router";

// 定义 mixin 
export function useRouterMixin() {
    const router = useRouter();
    
    // 路由跳转
    const toPage = (url) => {
        router.push(url); // 字符串形式跳转  //路由地址
    };

    return {
        toPage
    }
}


/*没啥用*/