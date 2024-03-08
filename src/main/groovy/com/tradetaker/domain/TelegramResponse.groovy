package com.tradetaker.domain

class Chat {
    public long id
    public String first_name
    public String last_name
    public String type
}

class From {
    public long id
    public boolean is_bot
    public String first_name
    public String username
}

class Result {
    public int message_id
    public From from
    public Chat chat
    public int date
    public String text
}

class TelegramResponse {
    boolean ok;
    Result result;
}
