package com.minosai.oneclick.util.service

//class UsageWorker @Inject constructor(var repo: OneClickRepo) : Worker() {
//
//    override fun doWork(): WorkerResult {
//        try {
//            val sessionLink = repo.getSessionLink()
//            sessionLink?.let { link ->
//                val document = Jsoup.connect(link).get()
//                val subTexts: Elements = document.getElementsByClass("subTextRight")
//                val usageElement = subTexts[subTexts.size -1]
//                val usage: String = usageElement.child(0).text()
//                repo.updateUsage(usage)
//            }
//            return WorkerResult.SUCCESS
//        } catch (e: Exception) {
//            e.printStackTrace()
//            return WorkerResult.FAILURE
//        }
//    }
//
//}