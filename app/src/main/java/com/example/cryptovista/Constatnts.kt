package com.example.cryptovista

// fragments' names
const val HOME_TITLE = "Top Coins"
const val NEWS_TITLE = "News"
const val EXPLORE_TITLE = "Explore"
const val PROFILE_TITLE = "Profile"

// not available data for display
const val NOT_AVAILABLE = "N/A"

// key for sending data from MainActivity to CoinDetailsActivity
const val FROM_MAIN_ACTIVITY = "from_main_activity"

// time durations
const val ONE_DAY: Long = 24 * 60 * 60
const val ONE_WEEK: Long = 7 * 24 * 60 * 60
const val TWO_WEEKS: Long = 2 * 7 * 24 * 60 * 60
const val ONE_MONTH: Long = 30 * 24 * 60 * 60
const val TWO_MONTHS: Long = 2 * 30 * 24 * 60 * 60
const val ONE_YEAR: Long = 365 * 24 * 60 * 60

// helpers for getting logs
const val FAILURE_ON_FETCHING_DATA = "failureOnFetchingData" // from server

// helpers for showing toast
const val SOMETHING_WENT_WRONG = "Something went wrong!\nPlease try again later..." // when fetching data fails
const val THERE_IS_NO_DATA = "There is no data!" // when the fetched data is null