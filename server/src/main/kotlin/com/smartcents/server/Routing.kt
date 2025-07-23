package com.smartcents.server

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.request.*
import com.smartcents.server.models.*

fun Application.configureRouting() {
    routing {
        // Health check endpoint
        get("/health") {
            call.respondText("Server is running!", ContentType.Text.Plain)
        }

        // API routes
        route("/api") {
            // Authentication routes
            route("/auth") {
                post("/login") {
                    val request = call.receive<LoginRequest>()
                    // In a real app, you would validate credentials here
                    call.respond(
                        AuthResponse(
                            token = "sample-jwt-token",
                            user = User(
                                id = "1",
                                name = "Sample User",
                                email = request.email
                            )
                        )
                    )
                }

                post("/signup") {
                    val request = call.receive<SignupRequest>()
                    // In a real app, you would create a new user here
                    call.respond(
                        AuthResponse(
                            token = "sample-jwt-token",
                            user = User(
                                id = "1",
                                name = request.name,
                                email = request.email
                            )
                        )
                    )
                }

                post("/forgot-password") {
                    val request = call.receive<ForgotPasswordRequest>()
                    // In a real app, you would send a password reset email
                    call.respond(HttpStatusCode.OK, mapOf("message" to "Password reset email sent"))
                }

                post("/reset-password") {
                    val request = call.receive<ResetPasswordRequest>()
                    // In a real app, you would validate the token and update the password
                    call.respond(HttpStatusCode.OK, mapOf("message" to "Password reset successful"))
                }
            }

            // User routes
            route("/users") {
                get {
                    call.respond(mapOf("users" to listOf(
                        User("1", "John Doe", "john@example.com"),
                        User("2", "Jane Smith", "jane@example.com")
                    )))
                }

                get("/{id}") {
                    val id = call.parameters["id"]
                    call.respond(User(id ?: "1", "User $id", "user$id@example.com"))
                }
            }

            // Transaction routes
            route("/transactions") {
                get {
                    call.respond(mapOf("transactions" to listOf(
                        Transaction(1, 100.0, "Groceries", "Food", "2023-05-15"),
                        Transaction(2, 50.0, "Gas", "Transportation", "2023-05-16")
                    )))
                }

                get("/{id}") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: 1
                    call.respond(
                        Transaction(
                            id = id,
                            amount = 100.0,
                            description = "Transaction $id",
                            category = "Miscellaneous",
                            date = "2023-05-15"
                        )
                    )
                }
            }

            // Quiz routes
            route("/quizzes") {
                get {
                    val quizzes = listOf(
                        Quiz(
                            id = 1,
                            title = "Financial Basics",
                            description = "Test your knowledge of basic financial concepts",
                            questions = listOf(
                                Question(
                                    id = 1,
                                    text = "What is compound interest?",
                                    options = listOf(
                                        "Interest calculated on the initial principal only",
                                        "Interest calculated on the initial principal and accumulated interest",
                                        "A fixed interest rate that never changes",
                                        "Interest that is compounded once a year"
                                    ),
                                    correctOptionIndex = 1,
                                    explanation = "Compound interest is calculated on both the initial principal and the accumulated interest from previous periods."
                                ),
                                Question(
                                    id = 2,
                                    text = "What is a budget?",
                                    options = listOf(
                                        "A restriction on spending",
                                        "A plan for saving and spending money",
                                        "A type of investment",
                                        "A type of loan"
                                    ),
                                    correctOptionIndex = 1,
                                    explanation = "A budget is a plan for how to save and spend your money based on your income and expenses."
                                )
                            ),
                            difficulty = "Beginner",
                            category = "Personal Finance"
                        ),
                        Quiz(
                            id = 2,
                            title = "Investment Strategies",
                            description = "Learn about different investment approaches",
                            questions = listOf(
                                Question(
                                    id = 3,
                                    text = "What is diversification?",
                                    options = listOf(
                                        "Investing all your money in one stock",
                                        "Spreading investments across various assets to reduce risk",
                                        "Investing only in high-risk assets",
                                        "Investing only in low-risk assets"
                                    ),
                                    correctOptionIndex = 1,
                                    explanation = "Diversification involves spreading investments across different asset classes to reduce risk."
                                )
                            ),
                            difficulty = "Intermediate",
                            category = "Investing"
                        )
                    )
                    call.respond(mapOf("quizzes" to quizzes))
                }

                get("/{id}") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: 1
                    val quiz = Quiz(
                        id = id,
                        title = "Quiz $id",
                        description = "Description for Quiz $id",
                        questions = listOf(
                            Question(
                                id = 1,
                                text = "Sample question for Quiz $id",
                                options = listOf("Option 1", "Option 2", "Option 3", "Option 4"),
                                correctOptionIndex = 1,
                                explanation = "Explanation for the correct answer"
                            )
                        ),
                        difficulty = "Beginner",
                        category = "Personal Finance"
                    )
                    call.respond(quiz)
                }

                post("/{id}/submit") {
                    val quizId = call.parameters["id"]?.toIntOrNull() ?: 1
                    val answers = call.receive<Map<Int, Int>>() // Question ID to selected option index

                    // In a real app, you would validate answers and calculate the score
                    val result = QuizResult(
                        quizId = quizId,
                        userId = "1",
                        score = 2,
                        totalQuestions = 3,
                        completedAt = "2023-05-17T14:30:00Z"
                    )
                    call.respond(result)
                }
            }

            // Quotes routes
            route("/quotes") {
                get {
                    val quotes = listOf(
                        Quote(1, "The best investment you can make is in yourself.", "Warren Buffett", "Investing"),
                        Quote(2, "Don't save what is left after spending; spend what is left after saving.", "Warren Buffett", "Saving"),
                        Quote(3, "Financial peace isn't the acquisition of stuff. It's learning to live on less than you make.", "Dave Ramsey", "Budgeting"),
                        Quote(4, "It's not how much money you make, but how much money you keep.", "Robert Kiyosaki", "Wealth Building"),
                        Quote(5, "A budget is telling your money where to go instead of wondering where it went.", "Dave Ramsey", "Budgeting")
                    )
                    call.respond(mapOf("quotes" to quotes))
                }

                get("/random") {
                    val quote = Quote(
                        id = 1,
                        text = "The best investment you can make is in yourself.",
                        author = "Warren Buffett",
                        category = "Investing"
                    )
                    call.respond(quote)
                }
            }

            // Educational content routes
            route("/articles") {
                get {
                    val articles = listOf(
                        Article(
                            id = 1,
                            title = "Understanding Compound Interest",
                            content = "Compound interest is the addition of interest to the principal sum of a loan or deposit...",
                            author = "Financial Expert",
                            publishDate = "2023-05-01",
                            category = "Investing",
                            tags = listOf("interest", "investing", "basics")
                        ),
                        Article(
                            id = 2,
                            title = "Budgeting 101",
                            content = "Creating a budget is the first step towards financial freedom...",
                            author = "Money Manager",
                            publishDate = "2023-05-05",
                            category = "Budgeting",
                            tags = listOf("budget", "planning", "basics")
                        )
                    )
                    call.respond(mapOf("articles" to articles))
                }

                get("/{id}") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: 1
                    val article = Article(
                        id = id,
                        title = "Article $id",
                        content = "Content for Article $id...",
                        author = "Author Name",
                        publishDate = "2023-05-01",
                        category = "Finance",
                        tags = listOf("tag1", "tag2")
                    )
                    call.respond(article)
                }
            }

            // Financial tips routes
            route("/tips") {
                get {
                    val tips = listOf(
                        Tip(1, "Start an Emergency Fund", "Aim to save 3-6 months of expenses in an easily accessible account.", "Saving"),
                        Tip(2, "Pay Yourself First", "Set aside a portion of your income for savings before paying bills.", "Budgeting"),
                        Tip(3, "Use the 50/30/20 Rule", "Allocate 50% of your budget to needs, 30% to wants, and 20% to savings.", "Budgeting")
                    )
                    call.respond(mapOf("tips" to tips))
                }

                get("/random") {
                    val tip = Tip(
                        id = 1,
                        title = "Start an Emergency Fund",
                        content = "Aim to save 3-6 months of expenses in an easily accessible account.",
                        category = "Saving"
                    )
                    call.respond(tip)
                }
            }
        }
    }
}