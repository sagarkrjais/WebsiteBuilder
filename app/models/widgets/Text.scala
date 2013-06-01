package models.widgets

import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._

case class Text(
		title: String,
		text: String,
		id: Long = -1)

object Text {
	
	val simple = {
		get[String]("text_widget.title") ~
		get[String]("text_widget.text") ~
		get[Long]("text_widget.text_widget_id") map {
			case title ~ text ~ text_widget_id => Text(title, text, text_widget_id)
		}
	}
	
	def getById(textWidgetId: Long): Option[Text] = {
		DB.withConnection { implicit connection =>
			SQL(
				"""
					select * from text_widget
						where text_widget_id = {text_widget_id}
				"""
			).on(
				'text_widget_id -> textWidgetId
			).as(Text.simple.singleOpt)
		}
	}
	
	def getByWidgetId(widgetId: Long): Option[Text] = {
		DB.withConnection { implicit connection =>
			SQL(
				"""
					select * from text_widget
						where widget_id = {widget_id}
				"""
			).on(
				'widget_id -> widgetId
			).as(Text.simple.singleOpt)
		}
	}
	
	def create(textWidget: Text): Long = {
		DB.withConnection { implicit connection =>
			SQL(
				"""
				insert into text_widget (title, text) values (
					{title}, {text}
				)
				""").on(
					'title -> textWidget.title,
					'text -> textWidget.text).executeInsert()
		} match {
			case Some(textWidgetId) => textWidgetId
			case None => -1
		}
	}
	
	def emptyTextWidget = {
		Text("", "")
	}
	
}