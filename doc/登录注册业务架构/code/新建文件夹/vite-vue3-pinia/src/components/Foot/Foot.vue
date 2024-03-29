<template>
    <div class="record">
        <div class="box">
            <div class="gongAn">
                <div>
                    <a target="_blank" class="webRecord"
                        href="http://www.beian.gov.cn/portal/registerSystemInfo?recordcode=13068202000222">
                        <img src="/image/imgs/record.png" />
                        <p>冀公网安备13068202000222号</p>
                    </a>
                </div>
                <p class="line"></p>
                <div class="ICP">
                    ICP证：<a href="https://beian.miit.gov.cn" target="_blank">冀ICP备2023004936号</a>
                </div>
            </div>
            <div>
            </div>
        </div>
    </div>
</template>

<style scoped lang="less">
.record {
    padding: 1rem 0;
    margin-top: 1rem;
    border-radius: .5rem;
    background-color: #fff;
    font-size: 1.2rem;
    color: var(--font-gray-color-dark);
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    align-items: center;
    font-size: 1.2rem;

    .box {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;

        .gongAn {
            display: flex;

            a {
                color: var(--font-gray-color-dark);
            }

            .line {
                margin: 0 1rem;
            }

            .webRecord {
                display: flex;
                justify-content: center;
                align-items: center;
            }

        }
    }

}

@media screen and (max-width: 800px) {
    .record {
        box-sizing: border-box;
        padding: 1rem;

        .gongAn {
            display: flex;
            flex-direction: column;
        }
    }
}
</style>