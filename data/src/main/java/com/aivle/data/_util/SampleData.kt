//package com.aivle.data._util
//
//import com.aivle.domain.model.sharingPost.Comment
//import com.aivle.domain.model.sharingPost.Reply
//import com.aivle.domain.model.sharingPost.SharingPost
//import com.aivle.domain.model.sharingPost.SharingPostDetail
//import com.aivle.domain.model.sharingPost.SharingPostItem
//import com.aivle.domain.model.user.User
//import kotlin.random.Random
//
//object SampleData {
//
//    fun getSharingPostItems(count: Int): List<SharingPostItem> = List(count) { i ->
//        SharingPostItem(i, user(i), imageUrl(300), "가전제품", "TV", "42인치 이상", "Sharing Post Title $i", "서울", "송파구", "잠실${i}동", "6월 ${i}일", "", Random.nextInt(0, 100), Random.nextInt(0, 100))
//    }
//
//    fun getSharingPostDetail(): SharingPostDetail {
//        val post = getSharingPost()
//        val comments = getComments(post.postId, post.user)
//        return SharingPostDetail(post, comments)
//    }
//
//    fun getSharingPost(): SharingPost = SharingPost(
//        1, user(0), "송파구 잠실1동", imageUrl(), "허먼밀러 뉴 에어론 체어, 풀, 그라파이트", "의자", LargeText, "createdDate", "expiredDate"
//    )
//
//    fun getComments(postId: Int, user: User) = List(5) { i ->
//        Comment(i, postId, user, SmallText, i * 3, "${i+1}시간 전")
//    }
//
//    fun getReplies(commentId: Int) = List(5) { i ->
//        Reply(i, commentId, user(i), VerySmallText, "${i+1}시간 전")
//    }
//
//    private fun user(userId: Int): User {
//        return User(userId, "010-0000-0000", "HongGilDong", "HongBurni")
//    }
//
//    private fun imageUrl(size: Int = 500): String {
//        return "https://picsum.photos/seed/${Random.nextInt(1000)}/$size"
//    }
//}
//
//private const val LargeText = """The standard Lorem Ipsum passage, used since the 1500s
//Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
//
//
//1914 translation by H. Rackham
//But I must explain to you how all this mistaken idea of denouncing pleasure and praising pain was born and I will give you a complete account of the system, and expound the actual teachings of the great explorer of the truth, the master-builder of human happiness. No one rejects, dislikes, or avoids pleasure itself, because it is pleasure, but because those who do not know how to pursue pleasure rationally encounter consequences that are extremely painful. Nor again is there anyone who loves or pursues or desires to obtain pain of itself, because it is pain, but because occasionally circumstances occur in which toil and pain can procure him some great pleasure.
//
//
//Section 1.10.33 of "de Finibus Bonorum et Malorum", written by Cicero in 45 BC
//At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga. Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod maxime placeat facere possimus, omnis voluptas assumenda est, omnis dolor repellendus. Temporibus autem quibusdam et aut officiis debitis aut rerum necessitatibus saepe eveniet ut et voluptates repudiandae sint et molestiae non recusandae."""
//
//private const val SmallText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
//private const val VerySmallText = "Lorem ipsum dolor sit amet, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."